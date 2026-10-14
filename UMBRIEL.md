# Umbriel

Umbriel é o jar de servidor híbrido deste projeto: mods e plugins rodando juntos,
reaproveitando a base de carregamento (Mixin) já existente neste repositório. O nome
vem de uma das lunas de Urano.

Toda a compatibilidade entre o modelo de item/entidade/evento nativo e o modelo
Bukkit é delegada a um componente separado, o **intérprete AkashicRecords** (apelido
curto: **Akasha**), para que qualquer outro servidor híbrido também possa importá-lo.

Domínio do projeto: `umbriel.blast.net.br`. Namespace Java do intérprete:
`br.net.blast.akashicrecords` (ver "AkashicRecords" abaixo).

## AkashicRecords (o intérprete)

Módulo `akashicrecords-api`: um conjunto pequeno de interfaces, sem amarras a uma
versão específica do Minecraft, que descreve o contrato de compatibilidade:

- `ItemBridge<N>` — item nativo ↔ `org.bukkit.inventory.ItemStack`.
- `EntityBridge<N>` — entidade nativa (mob incluso) ↔ `org.bukkit.entity.Entity`.
- `EventBridgeRegistry` / `EventBridge<N,B>` — evento nativo ↔ evento Bukkit, um
  bridge por tipo de evento nativo.
- `RegistryUnifierRegistry` / `RegistryUnifier<N,B>` — sincroniza um registry fechado
  do Bukkit (`Material`, `EntityType`, ...) com o registry nativo, aberto e
  extensível por mods.
- `AkashaPlatform` — o ponto de integração que um servidor híbrido implementa e
  publica via `ServiceLoader`; `Akasha.platform()` é como qualquer código alcança a
  implementação em execução sem depender de qual servidor a carregou.

Essas interfaces não dependem de nenhum código deste fork: o módulo é deliberadamente
desacoplado (namespace próprio, sem dependência de compilação no restante do
repositório), pensado para eventualmente ser extraído para um repositório próprio e
importado por qualquer servidor híbrido (Umbriel ou outro).

## Implementação concreta para MC 1.21.11

`arclight-common/.../mod/server/akashicrecords/Umbriel*` implementa o contrato do
AkashicRecords para esta base/versão, reaproveitando a infraestrutura de
CraftItemStack/CraftEntity/BukkitRegistry já existente em vez de duplicar o modelo de
item ou entidade:

- `UmbrielItemBridge` — envolve o `net.minecraft.world.item.ItemStack` nativo via
  `CraftItemStack.asCraftMirror`, sem copiar estado.
- `UmbrielEntityBridge` — usa o `getBukkitEntity()`/`getHandle()` que toda entidade
  nativa já expõe.
- `UmbrielMaterialUnifier` / `UmbrielEntityTypeUnifier` — leem as tabelas de
  `Material`/`EntityType` já unificadas no boot do servidor (todo item/mob registrado
  por um mod já ganha uma constante Bukkit sintetizada antes de qualquer plugin
  carregar).
- `UmbrielEventBridgeRegistry` / `UmbrielRegistryUnifierRegistry` — registries
  concretos (mapa simples) usados pelo `UmbrielAkashaPlatform`, publicado via
  `META-INF/services` sob o id `"umbriel"`.

Essa é a ideia central generalizada no AkashicRecords: tratar o item/entidade/evento
nativo como fonte única de verdade, e só construir pontes de acesso — não dois
modelos paralelos que podem divergir.

O que é específico da versão e precisa ser refeito a cada bump de Minecraft: os nomes
literais de componentes de dado, o layout de campos privados das classes utilitárias
do Bukkit, e as fábricas de metadata (`ItemMeta`/`BlockState`) amarradas às classes
`CraftMeta*` atuais.

## Roadmap

1. ~~`akashicrecords-api`: `ItemBridge`, `EntityBridge`, `EventBridgeRegistry`,
   `RegistryUnifierRegistry`, `AkashaPlatform`.~~ (feito)
2. ~~Implementação concreta para 1.21.11 (`Umbriel*` em `arclight-common`), publicada
   via `ServiceLoader`.~~ (feito)
3. Bridges de evento cobrindo pelo menos: uso de item, interação em bloco, dano a
   entidade, spawn de mob — por ora o `EventBridgeRegistry` está pronto mas vazio.
4. Cobertura de `RegistryUnifier` para os blocos/itens/mobs dos mods de teste.
5. Jar de servidor Umbriel: branding próprio do bootstrap, comando/log confirmando
   que plugin e mod veem o mesmo item/entidade através do AkashicRecords.
