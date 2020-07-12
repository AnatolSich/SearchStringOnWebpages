Задание
Написать web-приложение, которое производит поиск текста на web-страницах.
Поиск начинается со стартового url. Все ссылки найденные на этой странице используются 
для дальнейшего поиска - т.е. необходимо выполнить поиск текста по графу страниц. 
Использовать алгоритм поиска в ширину.
Закачка страниц - многопоточная, число потоков для одновременной закачки - 
параметризируется.
Входные данные:
1. Стартовый url
2. Максимальное количество потоков
3. Искомый текст
4. Максимальное количество сканируемых url
Графический интерфейс
Приложение должно содержать графический интерфейс, который позволяет:
1. Задавать следующие параметры:
1.1. Стартовый url
1.2. число потоков сканирования
1.3. искомый текст
1.4. максимальное количество сканируемых url
2. Показывать список сканируемых/отсканированных URL и статус сканирования - 
закачка/найдено/не найдено/ошибка (указать ошибку)
Дополнительно
1. Поскольку в постановке задачи может не хватать деталей, то вам необходимо 
будет делать предположения самостоятельно. Не забудьте указать предположения, 
которые вы сделали для того чтобы мы могли проверить на сколько задача корректно 
реализована с точки зрения ее понимания вами.
2. Напишите, как вы предлагаете тестировать приложение.


**Tool execution will look like:**

   java -jar Searcher.jar <input_origin_file_path> <target_string> 

**For example:**

    java -jar Searcher.jar samples/sample-0-origin.html everything-ok
    
 **In Main.class one can configure:**
 MAX_SEARCH_DEPTH - max level of web-tree for searching;
 THREADS_NUMBER - quantity of parallel threads for searching.
 
 LINK_STARTED_WITH - responsible for links pattern to be proceeded.
 
 **Process and results**
 
Every level of web-tree is proceeding consistently in depth (breadth-first search algorithm).
Because of required breadth-first search algorithm here can't be used 
pure ForkJoinPool (in which depth search algorithm is used).
External library Jsoup is used for parsing web pages and searching for text.
Process is multithreading and will finished when MAX_SEARCH_DEPTH will be reached.
All results can be received in log and in console. 
Log shows every step of process:
1.Initial data.
2. All urls which were found on every page and on every stage of search process
(stage number - number of level of web-tree in depth).
3. There is thread id in most log messages.
4. In the end one can see list of urls where required text is found and list of all proceeded urls.  


//TO DO

Basis of process is done.
Handling of exceptions must be added.
Logger must be replaced with lo4j.
If we need interaction with FrontEnd - code can be refined to MVC pattern with Spring framework.
Unit tests must be created for controller and services methods.
