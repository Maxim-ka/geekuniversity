#### Урок 2
## Базовые возможности фреймворка LibGDX
Работа с графикой. Векторная математика. Обработка логики игры.

Домашнее задание:
1. Разобраться с кодом
2. При запуске игры по карте раскидывается еда, должно быть 10 кусков
3. При поедании листов, они появляются в случайном месте карты, а персонаж вырастает на 0.02f (scale)


#### Урок 3
## Разработка каркаса игры
Домашнее задание:   
+  прописать мозги ботам.
#### Здесь и далее, пишется совсем другой проект, отличный от указанного в методичке. Если получиться найти того кто умеет рисовать или подходящие картинки, можно запилить отдельный законченный продукт 

#### Урок 4
## Оптимизация проекта
Оптимизация работы с памятью. Управление объектами через Emitter
классы. Использование паттерна Object Pool. Использование атласов
текстур.

Домашнее задание:   
+  Реализация FoodEmitter, случайная еда появляется каждые пол секунды

#### Урок 5 
## Доработка игровой логики
Домашнее задание:
1. Разбор кода
2. Реализовать паузу на кнопку "P"
3. *Попробуйте сделать мини-карту(зеленая точка - еда, красная - бот, оранжевая - игрок)
     Если не получится, опишите идею просто на словах
     ----------------------------------------------------------
План:
 1. Карта
 2. Служебные классы(Система экранов, экраны, интерфейс)
 3. Анимация
 4. Всякие камни, объекты которые нельзя съесть
 5. PowerUps
 
 #### Урок 6
 ## Управление «экранами»
Разделение игры на экраны. Менеджер экранов. Меню,
настройки, таблица результатов. Пользовательский интерфейс.
Переходы между экранами, освобождение ресурсов

Домашнее задание:
1. Разбор кода
2. Когда персонаж дорастает до определенного размера, необходимо сделать переход
на новую карту, на следующей карте шанс красной еды должен быть увеличен до 15%
3. Для тренировки: добавить на игровой экран кнопку паузы(чтобы можно было на нее
нажимать), реализация через Stage

#### Урок 7
## Работа с камерой и звуками
Решение проблемы разных разрешений экранов. Добавление
звуков и музыки

Домашнее задание:
1. Разбор кода
2. Сохранение игры, как бы Вы сохраняли состояние игры, идея на словах
3. *Реализация в коде

Сохранение игры через стандартную реализацию java.io.Serializable.
заменил картинку взрыва на анимацию частиц   

#### Урок 8
## Финальная доработка проекта
Доработка всех компонентов, сборка apk файла, тестирование
на устройстве

Домашнее задание:
1. Разбор кода
2. Добавить 5 жизней игроку, как только игрока съедают жизнь убавляется
3. Сделать GameOver Screen, на котором отобразить счет и уровень в игре
4. Кнопка на GameOver для возврата в меню
5. *Сделать High Score, с отображением в главном меню