# spring-es
Integration between Elasticsearch and Spring Framework with Elasticsearch native library (without Spring Data).
 
Current project provides possibility to store data in any storage and use ES engine for search and aggregation needs.
 Search is implemented by ES, then all other data is loaded from main storage and merged with data from ES. 
 
Steps to use this approach:
 1. Implement basic service 'AbstractAppService' and all it dependencies
 2. Implement main storage by providing implementation of 'AppDao'
 3. Setup ES as search engine and some other database as main storage (SQL/NoSQL)
 4. Create REST controller and call required methods for loading and filtering data.
 
Example is here: ...
