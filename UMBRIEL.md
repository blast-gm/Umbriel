# Umbriel

Umbriel é a camada de compatibilidade que este projeto está construindo para que
plugins Bukkit/Spigot/Paper e mods Forge/NeoForge/Fabric enxerguem **o mesmo mundo**
— mesmos itens, mesmos mobs, mesmos eventos — em vez de dois modelos paralelos que
só coincidem por acaso. O nome vem de uma das lunas de Urano.

Domínio do projeto: `umbriel.blast.net.br`. Namespace Java: `br.net.blast.umbriel`
(independente do namespace `io.izzel.arclight` do Arclight original — ver "Relação
com o Arclight" abaixo).

## Objetivo

1. **API (`umbriel-api`)**: um conjunto pequeno de interfaces, sem amarras a uma
   versão específica do Minecraft, que descreve o contrato de compatibilidade:
   como converter item nativo ↔ item Bukkit, entidade nativa ↔ entidade Bukkit,
   evento nativo ↔ evento Bukkit, e como sincronizar um registry fechado do Bukkit
   (`Material`, `EntityType`, ...) com o registry aberto e extensível por mods.
   Qualquer servidor híbrido (Arclight, Mohist, Cardboard, Magma, ou um futuro)
   pode publicar uma implementação via `ServiceLoader` e passar a falar "Umbriel".
2. **Implementação para MC 1.21.11**: a implementação concreta dessas interfaces
   para a versão atual, usando o mesmo padrão de bridge que o Arclight já comprovou
   funcionar em produção (ver estudo abaixo).
3. **Jar de servidor próprio**: usar o Arclight como base prática — ele já resolve
   o carregamento simultâneo de mods e plugins via Mixin — para produzir um jar de
   servidor que roda sobre o Umbriel, servindo como primeiro consumidor real da API.

## O que foi estudado no Arclight

Arclight não duplica o modelo de item: ele mantém o `net.minecraft.world.item.ItemStack`
nativo como única fonte de verdade e faz o `CraftItemStack` do CraftBukkit apontar
para ele, em vez de copiar estado para dois objetos que podem divergir. A "compatibilidade
perfeita" nasce de *não duplicar estado*, só de expor acesso a ele dos dois lados.

Pontos relevantes (arquivos do Arclight, para referência, não copiados):

- `arclight-common/.../mixin/core/world/item/ItemStackMixin.java` +
  `bridge/core/world/item/ItemStackBridge.java` — mixin na `ItemStack` nativa expondo
  métodos `arclight$...` para outro código mexer no mapa de componentes/NBT direto.
- `arclight-common/.../mod/server/BukkitRegistry.java` — ao subir o servidor, percorre
  os registries nativos (`BuiltInRegistries.BLOCK`/`ITEM`) e, para qualquer entrada
  registrada por mod, sintetiza uma constante de `Material` via injeção reflexiva de
  enum (`EnumHelper`) e corrige as tabelas internas do `CraftMagicNumbers` via `Unsafe`.
  O mesmo truque é reaplicado para `EntityType`, `World.Environment`, `PotionType` etc.
- `arclight-common/.../mixin/bukkit/MaterialMixin.java` +
  `bridge/bukkit/MaterialBridge.java` — mixin no próprio enum `Material` do Bukkit,
  acrescentando fábricas plugáveis de `ItemMeta`/`BlockState` para que um item modded
  tenha metadata coerente mesmo sem o CraftBukkit saber que ele existe.
- Padrão transversal: para uma classe-alvo `Foo`, existe uma interface `FooBridge`
  com métodos `bridge$xxx`; `FooMixin implements FooBridge`; e o resto do código faz
  `(FooBridge) foo` para chamar a ponte sem depender em tempo de compilação do mixin.
  **Esta é a ideia central que generalizamos em `umbriel-api`.**
- Eventos: interações de item do lado mod são redirecionadas para o pipeline de
  eventos que o Bukkit já tem (`CraftEventFactory`/`ArclightEventFactory`), em vez de
  criar um pipeline paralelo.

O que é específico da versão e precisa ser refeito a cada bump de Minecraft: os nomes
literais de `DataComponents`, o layout de campos privados do `CraftMagicNumbers` e a
tabela `TYPES` de `itemMetaFactory`/`blockStateFactory` amarrada às classes `CraftMeta*`
atuais do CraftBukkit.

## Relação com o Arclight

Este repositório é historicamente um fork do Arclight, mas o `umbriel-api` nasce como
um módulo deliberadamente desacoplado: namespace próprio (`br.net.blast.umbriel`), sem
dependência de compilação em `arclight-common`, e pensado para eventualmente ser
extraído para um repositório próprio. O que é reaproveitado do Arclight é o *padrão*
de bridge (interface + mixin) documentado acima — não o código GPL em si.

A implementação concreta para MC 1.21.11, e o jar de servidor que a consome, podem
viver dentro deste fork (reaproveitando o build Mixin/Loom já existente) sem que a API
em si fique amarrada a ele.

## Roadmap

1. ~~`umbriel-api`: interfaces `ItemBridge`, `EntityBridge`, `EventBridge`,
   `RegistryUnifier`, `UmbrielPlatform`.~~ (feito)
2. Implementação `umbriel-mc1_21_11` (ou módulo equivalente dentro de
   `arclight-common`) que publica um `UmbrielPlatform` concreto, reaproveitando os
   bridges do Arclight sob a nova interface.
3. Bridge de registry para `Material`/`EntityType` cobrindo os blocos/itens/mobs
   dos mods de teste.
4. Bridge de eventos cobrindo pelo menos: uso de item, interação em bloco, dano a
   entidade, spawn de mob.
5. Jar de servidor Umbriel: variante do bootstrap do Arclight que inicializa o
   `UmbrielPlatform` e expõe um comando/log confirmando que plugin e mod veem o
   mesmo item/entidade.
