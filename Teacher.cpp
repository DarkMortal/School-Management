#include <iostream>
#include <stdlib.h>
#include<iomanip>
#include<string.h>
#include<stdlib.h>
//Header Files for MySql
#include <mysql_connection.h>
#include <cppconn/driver.h>
#include <cppconn/exception.h>
#include <cppconn/resultset.h>
#include <cppconn/statement.h>
#include <mysql_driver.h>

using namespace std;
using namespace sql;
using namespace sql::mysql;

MySQL_Driver *driver;
Connection *con;
ConnectOptionsMap connection_properties;

bool isValidNum(string x){
   int len=x.length();
   if(len!=10){
     cout<<"Phone number can't be less or more than 10 numbers"<<endl;
     return false;
   }
   for(int i=0;i<len;i++){
      if(isalpha(x.at(i))){
         cout<<"Phone number can't contain alphabets"<<endl;
         return false;
      }
   }
   unique_ptr<Statement>stmt(con->createStatement());
   unique_ptr<ResultSet>res(stmt->executeQuery("SELECT *from Teacher_LOGIN WHERE Phone='"+x+"';"));
   if(res->rowsCount()==1){
     cout<<"Phone number already present"<<endl;
     return false;
   }
   return true;
}

bool isValidID(string x){
   std::unique_ptr< sql::Statement > stmt(con->createStatement());
   std::unique_ptr< sql::ResultSet >res(stmt->executeQuery("SELECT *from Teacher_LOGIN WHERE Email='"+x+"';"));
   if(res->rowsCount()==1){
     cout<<"Email already present"<<endl;
     return false;
   }
   return true;
}

class Teacher{
   string name,eid,phone,cls;
   char pass[100];
   public:void getData(){
      ED: cout<<"Enter Email : "; cin>>eid;
      if(!isValidID(eid)) goto ED;
      cin.ignore( numeric_limits <streamsize> ::max(), '\n');
      cout<<"Enter Name : "; getline(cin,name);
      phone: cout<<"Enter Phone : "; cin>>phone;
      if(!isValidNum(phone)) goto phone;
      CLS: cout<<"Enter Class : "; cin>>cls;
      if(cls.length()>5) goto CLS;
      strcpy(pass,getpass("Enter the Password : ")); cout<<endl;
   }
   string getUpdateQuery(){
      string res="('"+eid+"','"+name+"','"+pass+"','"+phone+"','"+cls+"')";
      return res;
   }
};

void ShowAll(){
   int cnt=0;
   std::unique_ptr< sql::Statement > stmt(con->createStatement());
   std::unique_ptr< sql::ResultSet >res(stmt->executeQuery("SELECT *from Teacher_LOGIN"));
   while (res->next())
   {
      cnt++;
      cout<<"Email : "<<res->getString("Email")<<endl;
      cout<<"Name : "<<res->getString("Name")<<endl;
      cout<<"Phone : "<<res->getString("Phone")<<endl;
      cout<<"Student count : "<<res->getInt("Students")<<endl;
      cout<<"Class : "<<res->getString("Class")<<endl<<endl;
   }
   cout<<"There are "<<cnt<<" records in total"<<endl;
}

void Insert(int n){
  Teacher *x;
  string query="INSERT into Teacher_LOGIN (Email,Name,Password,Phone,Class) values ";
  for(int i=0;i<n;i++){
    x=new Teacher;
    x->getData();
    query+=x->getUpdateQuery();
    if(i!=n-1) query+=",";
    else query+=";";
    delete x;
  }
  std::unique_ptr< sql::Statement > stmt(con->createStatement());
  stmt->execute(query);
}

void DELETE(string eid){
   std::unique_ptr< sql::Statement > stmt(con->createStatement());
   std::unique_ptr< sql::ResultSet >res(stmt->executeQuery("SELECT *from Teacher_LOGIN WHERE Email='"+eid+"';"));
   if(res->rowsCount()==1){
     stmt->execute("DELETE from Teacher_LOGIN WHERE Email='"+eid+"';");
     cout<<"\nRecord deleted successfully!!!"<<endl;
   }
   else cout<<"\nRecord doesn't exist!!!"<<endl;
}

void FIND(string eid){
   std::unique_ptr< sql::Statement > stmt(con->createStatement());
   std::unique_ptr< sql::ResultSet >res(stmt->executeQuery("SELECT *from Teacher_LOGIN WHERE Email='"+eid+"';"));
   if(res->rowsCount()==1){
      res->next();
      cout<<"ID : "<<res->getString("Email")<<endl;
      cout<<"Name : "<<res->getString("Name")<<endl;
      cout<<"Phone : "<<res->getString("Phone")<<endl;
      cout<<"Class : "<<res->getString("Class")<<endl<<endl;
   }
   else cout<<"\nRecord doesn't exist!!!"<<endl;
}

int main() {
    try{
        //Create a connection
        char password[20];
        strcpy(password,getpass("Enter the Password : "));
        driver = get_mysql_driver_instance();
        con = driver->connect("tcp://127.0.0.1:3306", "User_69", password);
        con->setSchema("JavaSE");

        start: system("clear");
        int opt=0; string data; char ch=0;
        cout<<"1. Enter new Records"<<endl<<"2. Update an Existing record"<<endl<<"3. Delete an Existing record"<<endl<<"4. View an existing Record"<<endl<<"5. Show all Existing Records"<<endl;
        cin>>opt;
        switch(opt){
           case 1:{
              cout<<"Enter the number of Records you want to enter : "; cin>>opt;
              Insert(opt);
              cout<<opt<<" records inserted successfully!!!"<<endl;
              break;
           }
           case 2:{
              cout<<"Enter the Email you want to update : "; cin>>data;
              DELETE(data); Insert(1);
              cout<<"Record updated successfully!!!"<<endl;
              break;
           }
           case 3:{
              cout<<"Enter the Email you want to delete : "; cin>>data;
              DELETE(data); break;
           }
           case 4:{
              cout<<"Enter the Email you want to see : "; cin>>data;
              FIND(data); break;
           }
           case 5:{
              ShowAll(); break;
           }
           default: cout<<"\nPlease choose a valid option"<<endl;
        }
        cout<<"Do you want to continue? (Y/N) : "; cin>>ch;
        if(ch=='Y'||ch=='y') goto start;
        else goto END;
    }
    catch(...){
        cout<<"Connection to external database failed"<<endl<<"Please Enter the correct Password"<<endl;
    }
    END: return 0;
}
