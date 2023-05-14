# Многопоточный сетевой чат

---

## Задача
Данный проект является курсовой работой. С заданием можно
ознакомиться [здесь](https://github.com/netology-code/jd-homeworks/blob/master/diploma/networkchat.md)
## Описание архитектуры проекта
Согласно заданию решение состоит из двух приложений: сервера и клиента(Консольный интерфейс, 
в дальнейшем будет написан оконный интерфейс на JavaFX).
### Клиентское приложение
Клиентское приложение реализовано в виде двух потоков: основного(Client) и слушающего сервер(ServerListener).

#### Основной поток(Client) выполняет следующие задачи:
1. Зачитывает настройки приложения из файла настроек, который создает и заполняет сервер.
2. Инициализирует приложение: создает clientLog.log для записи логов
клиента, хранящего сообщения чата и основные действия приложения, а также устанавливает имя клиента для чата.
   (Для отладки и тестирования адрес для clientLog.log устанавливается через конструктор класса Client)
3. Выполняет подключение к серверу в соответствии с параметрами, зачитанными из файла настроек
и ранее установленного имени.
4. Обеспечивает ввод пользователя сообщений из консоли и отправку их на сервер.
5. При введении команды "/exit" инициализируется выход из приложения.

#### Слушающий сервер поток(ServerListener) выполняет следующие задачи:
1. Слушает сервер на предмет появления сообщений от сервера.
2. При получении сообщений от сервера сохраняет их в файл клиентского лога, а также выводит
их в консоль приложения.

Задачи ввода сообщений пользователем и вывода сообщений присланных сервером разнесены по двум потокам для
обеспечения асинхронности этих двух операций.

### Серверное приложение
Серверное приложение реализовано в виде двух потоков: основного(Server) и обработчика клиента(ClientHandler).
#### Основной поток(Server) выполняет следующие задачи:
1. Создает файл settings.txt, если он не был создан ранее. Перезаписывает в него настройки для подключения клиентов.
2. Создает serverLog.log, если он не был создан ранее. Для записи логов сервера и сообщений чата и основные действия
приложения.
3. Запускает сервер и ожидает подключений клиентов.
4. При подключении клиента передает его обработчику клиента(ClientHandler) и записывает его в список подключенных 
клиентов. 
5. При выходе клиента из чата удаляет обработчик клиента из списка подключенных клиентов

#### Обработчик клиента(ClientHandler) выполняет следующие задачи:
1. Устанавливает полученное имя клиенту для чата.
2. Отправляет всем активным участникам чата из списка подключенных клиентов сообщение о присоединении данного 
3. клиента (далее активные клиенты)
4. Ожидает сообщение от клиента, при получении отправляет его активным клиентам.
5. При получении команды "/exit" от клиента отправляет сообщение активным клиентам о том, что данный клиент покинул чат.
Поток прерывается и удаляет себя из списка подключенных клиентов.
6. Все сообщения и основные действия приложения сохраняются в файл серверного лога.

Задачи ввода ожидания подключения и обработку каждого клиента разнесены по разным потокам для
обеспечения асинхронности этих операций (Подключении новых клиентов и обработку сообщений всех подключившихся
клиентов).

### Состав проекта:
Проект выполнен в виде приложения Maven, состоящий из:
1. client - содержит клиентское приложение.
2. logger - содержит логгер, который используется клиентом и сервером в едином экземпляре. Реализован по шаблону Singleton.
3. server - содержит серверное приложение.


### Тесты:
Мануальным тестированием проведены интеграционные тесты сервера с подключением нескольких пользователей. Для этого выполнено:
1. Созданы классы "MainClient1" и "MainClient1"
2. Конструктор клиента принимает адрес файла для логирования (для того, чтобы логи конкретного пользователя
записывались в собственный лог файл).

Unit-тестами покрыт только класс Logger.
