import java.util.HashMap;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import openCSV;

String text1;

int l=0;
StringList v=new StringList();
Database d=new Database(p);



 public class Database {
  StringList log;
  String backupPath;
  String storagePath;
  ArrayList<Link> references;

  List tables;

  int allocated;
  int used;
  int disk;
  int depth;
  boolean auto;
  Database() {



  } // Database (constructor)



  public void query(int table, int column, int row, StringList r) {
    TableRow z;
    boolean m;
    try {

      tables.get(table).getInt(row, column);
      tables.get(table).getString(row, column);
      tables.get(table).getFloat(row, column);
      m=false;
    }
    catch(NullPointerException n) {
      n.printStackTrace();
      log.append(n.printStackTrace());
      m=true;
    }
    if (m) {
      tables.add(tables.size()+1, new Table());
      tables.set(tables.size()-1, loadTable(storagePath+table+".txt"));
      z=tables.get(table).getRow(row);
      for (int i=0; i<tables.get(table).getColumnCount(); i++) {
        r.append(z.getString(i));
      }
    } else {

      z=tables.get(table).getRow(row);
      for (int i=0; i<tables.get(table).getColumnCount(); i++) {
        r.append(z.getString(i));
      }
    }
  }



  public void search(int column, int table, boolean containing, String search) {
    StringList s=new StringList();

    if (containing) {
      for (TableRow r : tables.get(table).findRows(search, column)) {
        String m=new String();
        for (int i=0; i<tables.get(table).getColumnCount(); i++) {
          m+=r.getString(i);
          if (i==tables.get(table).getColumnCount()) {
            s.append(m);
          }
        }
      }
    } else {
      for (TableRow r : tables.get(table).matchRows(search, column)) {
        String m=new String();
        for (int i=0; i<tables.get(table).getColumnCount(); i++) {
          m+=r.getString(i);
          if (i==tables.get(table).getColumnCount()) {
            s.append(m);
          }
        }
      }
    }
  }



  public void makeLink(int a, int b, boolean c) {
    references.add(references.size()+1, new Link());
    references.get(references.size()).setR(a, b, c);
  }



  public void addColumn( int table, String name, String type, int m) {
    tables.get(table).addColumn(name);
  }
  public void loadData( String data) {
    Table t;
     t=loadTable(data);
    tables.add(tables.size()+1,t);

  }



  public void add(String[] t, int table) {
    tables.get(table).addRow();
    /*for(int i=0;i<tables.get(table).getColumnCount();i++){
     tables.get(table).setString(i,t[i]);
     }*/
  }



  public void config(int allocate, int depth2, boolean auto2, String filepath, String filepath2) {
    storagePath=filepath;
    backupPath=filepath2;
    allocated=allocate;
    depth=depth2;
    auto=auto2;
  }
  /*public void do() {
   }*/



  public void backup() {
    boolean nb;
    String timeStamp=str(year())+":"+str(month())+":"+str(day())+":"+str(hour())+":"+str(minute())+":"+str(second());
    Table m=new Table();
    StringList stamps=new StringList();
    StringList status=new StringList();
    status.append(loadStrings(storagePath+"status.txt"));
    for (int i=0; i<tables.size(); i++) {
      try {
        m=loadTable(storagePath+i+".csv");
        nb=false;
      }
      catch(NullPointerException n) {
        n.printStackTrace();
        log.append(n.printStackTrace());
        nb=true;
      }
      if (nb&&status.get(i)!="del") {
        stamps.append(loadStrings(backupPath+"backups.txt"));
        for (int v=1; v<stamps.size(); v++) {
          boolean p;
          try {
            m=loadTable(storagePath+stamps.get(stamps.size()-v)+".txt");
            p=false;
          }
          catch(NullPointerException n) {
            n.printStackTrace();
            log.append(n.printStackTrace());
            p=true;
          }
          if (!p) {
            saveTable(m, backupPath+":"+timeStamp+":"+i+".csv");
            m.clearRows();
            break;
          } else {
            continue;
          }
        }
      }
      saveTable(m, backupPath+":"+timeStamp+":"+i+".csv" );
      m.clearRows();
      if (i==tables.size()) {
        stamps.append(loadStrings(backupPath+"backups.txt"));
        stamps.append(timeStamp);
        String[] n = new String[stamps.size()];
        for (int q=0; q<stamps.size(); q++) {
          n[q]=stamps.get(q);
          if (q==stamps.size()) {
            saveStrings(backupPath+"backups.txt", n);
          }
        }
      }
    }
  }


  public void restore(int table, int backup) {
    String filepath= "/Volumes" + File.separator
                                + "Users" + File.separator
                                + System.getProperty("user.name") + File.separator
                                + "Github" +  File.separator
                                + "J-DB"+ File.seperator
                                +"Dockers"+File.seperator
                                +"Database Instance"+File.seperator
                                +"files"+File.seperator
                                +stamps;
    BufferedReader b=new BufferedReader(new FileReader(filepath));

    StringList stamps=new StringList();
    stamps.append(loadStrings(backupPath+"backups.txt"));

    m=loadTable(backupPath+stamps.get(backup)+table+".csv");
    saveTable(m, storagePath+table+".txt");
  }
} // public class Database
/*
public void setup() {
  fullScreen();
  while(true){
    for (int i=0; i<height; i--) {
      text(v.get(v.size()-i), width/3, height-(i*15));
    }

  }
}


public void packetEvent(CarnivorePacket c){
 trigger=true;
 }
public void keyPressed() {
  background(150, 150, 150, 150);
  textSize(15);
  for (int i=0; i<height; i--) {
    text(v.get(v.size()-i), width/3, height-(i*15));
  }
  text1+=key;
  if (key==BACKSPACE) {
    if (text1.length()>0) {
      text1=text1.substring(0, text1.length()-1);
    } // if
  } // if
  if ((key==RETURN || key==ENTER)) {
    l++;
    StringList m=new StringList();
    m.append(split(text1, " "));
    if (m.get(1)=="load") {
      d.loadData(m.get(3));
      v.append(text1);
      m.clear();
      text1="";
    }
  }
}
*/
