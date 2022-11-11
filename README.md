# Itemhunt
指定されたアイテムを一番多く集めて勝利しよう

## Depends
[CommandAPI](https://www.spigotmc.org/resources/api-commandapi-1-13-1-19-2.62353/)

## Commands
### `/itemhunt[ih] start`
ゲームを開始します

### `/ih import`
config.ymlからポイントのデータをインポートします

### `/ih settings <key>`
ゲームの設定を行います  
設定項目は以下の通りです  
* `targets` フェーズごとに出現する目標アイテムの目安です
* `phase_time` 1フェーズあたりの時間(秒)です
* `phases` ゲーム全体のフェーズ数です
* `materials` 出現する目標アイテムです