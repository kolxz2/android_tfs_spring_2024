## Домашнее задание по лекции "Введение в тестирование"

#### Настроить окружение

- Запускать тесты непосредственно для тестируемого экрана. Для активити использовать ActivityScenario, для фрагмента - FragmentScenario
- Сетевые запросы обязательно мокировать. Предпочтительнее использовать мок сервер, но использование своего интерсептора или аналогичных бибилиотек допускается

Дополнительно:
- Настроить переиспользуемое правило для тестов
- Настроить TestOptions
- Настроить оркестрацию тестов

#### Покрыть тестами функционал экрана чата

- UI Happy-path для критических путей, пример: отправка сообщения, добавление реакции и так далее
- UI Integration для точек входа, пример: открытие экрана чата по нажатию на канал. Важно проверять, что передаются правильные аргументы (для активити использовать KIntent)
- UI для базовой проверки отображения, пример: элементы экрана отображаются
- Unit для бизнес- и ui-логики: тесты на Reducer, Actor и UiStateMapper

Дополнительно:
- Покрыть unit-тестами интеракторы, репозитории и так далее
- Покрыть тестами остальные экраны

#### Примеры чек-листов для написания тестов

**UI Happy-path:**

1. Вводим текст в поле сообщения
2. Нажимаем на кнопку отправки
3. Вызвался метод отправки сообщения
4. Сообщение отображается в списке

**UI Integration:**

1. Нажимаем на кнопку топика
2. Нажимаем на кнопку канала
3. Произошел переход на экран чата (проверить аргументы)

**UI:**

1. Метод получения сообщений вернул список
2. Отображается заголовок канала
3. Отображается список сообщений

**Unit:**

- Проверки конечных state, effects, commands в зависимости от event и state