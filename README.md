# <ins>School-Management using Java & C++</ins><br>
### JDBC Connector/J version 8.0.26 is used

### This project contains 2 modules :- One for adding teachers to the database through the CLI (made using C++) and the other one for adding students to the database (made using Java)

## __Some common errors you might run into__
* ### <ins><b>load_file(<filepath>)</b> returning null upon execution</ins><br>
  1. First you have to disable AppArmor for Linux (This error only comes in Linux)<br>You can read more on [how to disable AppArmor for mysql on Linux](https://askubuntu.com/questions/1144497/how-to-disable-apparmor-for-mysql)
  2. Next we have to change the value of <b>secure_file_priv</b> to the directory from where we want to load the file<br>To see the current value of <b>secure_file_priv</b> use the command<br>
  > mysql> SHOW VARIABLES like "secure_file_priv";
    
* ### <ins><b>Undefined reference to get_driver_instance in MySQL C++</ins><br>
  Be sure to use this linker while compiling the source file : <b>-lmysqlcppconn</b><br>
  This library contains the definition of all the necessary functions
    
* ### <ins><b>Adding resources to Java Project</b></ins>
  [This video will be helpful](https://www.youtube.com/watch?v=yksgU4SxoJY)

## <ins><b>Database Structure</b></ins>  
    ├── JavaSE
    ├──── Students
    └──── Teacher_LOGIN
  
## __Maven Dependencies__

    <dependencies>
       <dependency>
         <groupId>org.apache.poi</groupId>
         <artifactId>poi</artifactId>
         <version>5.0.0</version>
      </dependency>
      <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.23</version>
      </dependency>
      <dependency>
        <groupId>org.apache.poi</groupId>
        <artifactId>poi-ooxml</artifactId>
        <version>5.0.0</version>
      </dependency> 
      <dependency>
        <groupId>com.toedter</groupId>
        <artifactId>jcalendar</artifactId>
        <version>1.4</version>
      </dependency>
    </dependencies>
 
# <ins>Command to install MySQL C++ connector in Ubuntu</ins>
  sudo apt-get install libmysqlclient-dev
  
# [App Demo](https://youtu.be/2jop8gdODKg)
