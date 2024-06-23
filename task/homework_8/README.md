## Домашнее задание по Dagger2

**1. Реализовать компоненты приложения**
Создать компоненту с жизненным циклом (ЖЦ) всего приложения, где как синглтоны будут "жить" apiService-ы и другие "тяжелые" объекты на ваше усмотрение. Для каждого экрана создать Component или Subcomponent, в рамках которых будут "жить" зависимости с ЖЦ экрана (например, Subcomponent, которая будет предоставлять зависимости в ваши view)

**На всякий случай напоминаю**:
- Чистота оформления build.gradle-файла (не добавляйте лишние зависимости, удаляйте ненужные)
- Удалите папки test & androidTest – пока у вас нет тестов, эти папки вам не нужны
- Следите за чистотой кода, старайтесь избегать констант, состоящих из одной буквы и осмысленно называйте переменные
- Для домашки необходимо форкнуть мастер(если не делали этого ранее) и создать ветку hw_8. По завершении необходимо hw_8 направить на master и сделать merge request