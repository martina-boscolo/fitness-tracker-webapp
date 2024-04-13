# Instructions

## Setup

### Docker
- docker compose
- To fix Tomcat problem:
  - `docker ps` to list active containers
  - `docker exec -it <container_name> bash`
  - `mv webapps.dist/* webapps/`
  - `apt-get update && apt-get install vim -y`
  - `vi webapps/manager/META-INF/context.xml`
  - comment the section Valve with tag `<!-- -->`

### Database
- To refresh database:
  - Remove local directory `Docker`
  - Delete the postgres container
  - Compose the postgres container

### War package
- With maven in `Lifecycle`:
  - clean
  - compile
  - package
- Now a .war file should be created inside `target` dir

### Tomcat Deploy
- From localhost:8080 tomcat page
- Login into manager app
- Undeploy the war if already up
- Select from deploy menu the file to be deployed `target/` -> `.war`
- Deploy the war

## Dev Instuctions
### `main`
#### `database`:
- contains the initial sql instruction to be excecuted on docker compose

#### `java/it/dei/unipd/cyclek`
- **dao**: 
  - **AbstractDAO** and **DataAccessObject** should not be modified
  - every sql table should have a separate folder
  - inside each folder we can make as many classes we want to access database (one for query)
  - (look GetUserDAO, we generalized the get to be parametrized)
- **resources**:
  - Do not modify anything, just add a class like User that represent a sql table
  - Every Class must implement a writeJSON (See User implementation for more details)
- **rest**: 
  - **AbstractRR** and **RestResource** should not be modified
  - This is the most used package depend on what Service call we need as many Classes here
- **service**: 
  - Do not modify the **AbstractService**
  - Implement the method called by RestDispatcherServlet
  - This makes the URL parsing and calls the needed rest function
  - One file for table, like resources
- **servlet**: 
  - No file should be added here
  - Just modify the RestDispatcherServlet by adding an if condition with your Service Method

#### `resources`:
- DO NOT TOUCH THIS FOLDER

#### `webapp`:
- Will be used for frontend

### `test`
used for test
