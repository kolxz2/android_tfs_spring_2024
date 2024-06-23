# TFS Android Spring 2024

### Краткая информация о приложении
- Для навигации использовал Cicerone 
- Паттерн для слоя представлена MVI
- Для экранов: чат, все стримы, подписанные стримы и профили – реализован long polling. Если в процессе нахождения на экране произойдёт разрыв соединения, то появятся snackbar с запросом на переподключение. После, если соединение с интернетом снова установиться, то соединение для работы с long polling само установиться благодаря NetworkChangeReceiver (ConnectivityManager) из :data
- Для экранов со всеми стримами и подписанными реализован отдельный поиск. Последний поисковый запрос запоминается при переключениях между этими экранами
- Настроена локализация для английского и русского 
- На экране peoples в начале приходят данные из БД и у каждого будет статус оффлайн. Когда данные прийдут из сети список будет отсортирован по онлайн статусу и имени, также как это сделано web версии zulit. 
- Список стримов сортируется по названию
- Переопределены нажатия на кнопку назад по примеру WhatsApp. Логика: 
    * если при нажатии назад мы не на вкладке с каналами, то навигируемся на них
    * если во ViewPager экран со всеми стримами, то возвращаемся на подписанные стримы 
    * если мы на подписанных экранах, то выходим из приложения 


### Описание модулей

На лекции сказали, что задание будет интереснее, если разбить на модули, поэтому сделал разделение на модули:
- :app отвечает за создание синглтонов (Retrofit, Database, навигации) и корневых элементов для экранов (MainActivity внутри которой открывается сразу TabsMenuFragment. TabsMenuFragment является контейнером для переключения экранов через BottomNavigationView. Экраны с чатом и детальной информацией о пользователях открываются поверх TabsMenuFragment внутри контейнера MainActivity).
- :data хранит в себе логику работу в БД и Retrofit: модели данных, интерфейсы для взаимодействия. 
- :core. В :ui_kit хранятся вся переиспользуемая разметка и компоненты ui. В :navigation описаны интерфейсы для того, чтобы изнутри feature можно было осуществить навигацию, но саму реализацию скрыть в реализации :app. В :mvi лежит реализация MVI из семинара по архитектуре плюс дополнительный класс позаимствованный для переключения флоу из Elmslie – [Switche](https://github.com/vivid-money/elmslie/blob/elmslie-3-0/elmslie-core/src/main/java/vivid/money/elmslie/core/switcher/Switcher.kt). :data_di хранит интерфейсы которые реализует Application в :app. Нужен для доставления зависмостей Retrofit и Database в feature модули.
- :feature. в этом модули хранятся реализации всех экранов. 

