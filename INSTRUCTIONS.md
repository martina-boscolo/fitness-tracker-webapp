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
Our pages, except for login/signup, are composed of a navbar, the main content and a footer.
The three part are defined in html folder and the navbar/footer are inside the template folder since are common for every page.
The final result should consist in a jsp page that includes the navbar, the main and the footer.

- **css**:
  - contains a general style.css file that defines some common properties
  - contains a file for each page with specific properties
- **html**:
  - contains the template folder for the common navbar and footer component.
  - contains a file for each page that implements the main functionality of a page.
- **js**:
  - contains a utils file with common functions and constants to facilitate login/logout management and centralize ULR declaration.
- **jsp**:
  - contains a page for each service.
