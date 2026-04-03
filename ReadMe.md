![Android](https://img.shields.io/badge/Android-%233DDC84.svg?style=for-the-badge&logo=android&logoColor=white) ![Kotlin 2.3.0](https://img.shields.io/badge/Kotlin-2.3.0-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white) ![Compose 1.10.0](https://img.shields.io/badge/Compose-1.10.0-%234285F4.svg?style=for-the-badge&logo=jetpackcompose&logoColor=white) ![Bluetooth](https://img.shields.io/badge/Bluetooth-%230082FC.svg?style=for-the-badge&logo=bluetooth&logoColor=white)

## О проекте
**BTchat** - это Android мессенджер с открытым кодом, позволяющий коммуницировать посредством Bluetooth.

## Архитектура
Здесь используется слоистая многомодульная архитектура. Слой бизнес-логики разделён на 3 модуля: `:domain`, `:core:interfaces` и общий для них `:core:model`. Это позволяет ускорить сборку тем, что data-модули не будут проверяться при изменении `:domain`, а также логически разделить код.

На UI-слое за основу архитектуры взят MVI.

Так же используется `build-logic` модуль для сборки, в котором расположен вспомогательный класс `Config` и Convention-плагины, для переиспользования конфигурации.
 
![](./res/arch.svg)
