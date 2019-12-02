import com.intellij.database.model.DasTable
import com.intellij.database.util.Case
import com.intellij.database.util.DasUtil

import javax.swing.*;

/*
 * Available context bindings:
 *   SELECTION   Iterable<DasObject>
 *   PROJECT     project
 *   FILES       files helper
 */

packageName = "com.sample;"
typeMapping = [
        (~/(?i)int/)                      : "Long",
        (~/(?i)float|double|decimal|number|real/): "BigDecimal",
        (~/(?i)datetime|timestamp/)       : "Date",
        (~/(?i)date/)                     : "Date",
        (~/(?i)time/)                     : "java.sql.Time",
        (~/(?i)/)                         : "String"
]

//生成model类
FILES.chooseDirectoryAndSave("Choose directory", "Choose where to store generated model class") { dir ->
  SELECTION.filter { it instanceof DasTable }.each { generate(it, dir) }
}

//生成tkmybatis dao类
FILES.chooseDirectoryAndSave("Choose directory", "Choose where to store generated dao interface") { dir ->
  SELECTION.filter { it instanceof DasTable }.each { generateDao(it, dir) }
}

def generateDao(table, dir) {
  def className = javaName(table.getName(), true)
  new File(dir, className + "Mapper.java").withPrintWriter("utf-8") { out -> generateDao(out, table.getName(), className) }
}

def generateDao(out, tableName, modelName) {
  String daoTemplate = "package com.xjgj.services.mybatis.dao;\r\n" +
          "\r\n" +
          "import com.xjgj.show.common.model.${modelName};\r\n" +
          "import com.xjgj.show.common.utils.MyMapper;\r\n" +
          "import org.apache.ibatis.annotations.Mapper;\r\n" +
          "\r\n" +
          "@Mapper\r\n" +
          "public interface ${modelName}Mapper extends MyMapper<${modelName}> {\r\n" +
          "\r\n" +
          "}";
  out.println daoTemplate
}

def generate(table, dir) {
  def className = javaName(table.getName(), true)
  def fields = calcFields(table)
  new File(dir, className + ".java").withPrintWriter("utf-8") { out -> generate(out, table.getName(), className, fields) }
}

def generate(out, tableName, className, fields) {
  out.println "package $packageName"
  out.println ""
  out.println ""

  out.println "import lombok.Data;"

  out.println "import java.util.Date;"
  out.println "import java.math.BigDecimal;"
  out.println ""
  out.println "import javax.persistence.Id;"
  out.println "import javax.persistence.Table;"
  out.println "import javax.persistence.Entity;"
  out.println "import javax.persistence.Column;"
  out.println ""
  out.println ""

  out.println "@Data"
  out.println "@Entity"
  out.println "@Table(name = \"${tableName}\")"
  out.println "public class $className {"
  out.println ""
  fields.each() {
    if (it.comment != "") {
      out.println "  //${it.comment}"
    }
    //判断是否为主键,是则加入@Id这个annotation
    //暂时未解决主键问题

    if (it.annos != "") out.println "  ${it.annos}"
    out.println "  private ${it.type} ${it.name};"
    out.println ""
  }
  out.println ""
//  fields.each() {
//    out.println ""
//    out.println "  public ${it.type} get${it.name.capitalize()}() {"
//    out.println "    return ${it.name};"
//    out.println "  }"
//    out.println ""
//    out.println "  public void set${it.name.capitalize()}(${it.type} ${it.name}) {"
//    out.println "    this.${it.name} = ${it.name};"
//    out.println "  }"
//    out.println ""
//  }
  out.println "}"
}

def calcFields(table) {
  def fields = []
  def cols = []
  DasUtil.getColumns(table).reduce([]) { fields2, col ->
    cols.add(col)
    //JOptionPane.showMessageDialog(null, cols);
  }
  for(col in cols) {
    //DasUtil.getColumns(table).each {
    //add by yxy:一些共同的字段需要过滤掉，不要生成到model类中，而是从父类继承
    if(!(col.getName() in ["IDxxxdddgfgggsadagagadgd"])) {
      //end
      def spec = Case.LOWER.apply(col.getDataType().getSpecification())
      //add by yxy: spec: number(18,6)
      def typeStr = typeMapping.find { p, t -> p.matcher(spec).find() }.value
      fields.add([
              name : javaName(col.getName(), false),
              type : typeStr,
              annos: "@Column(name = \"${col.getName()}\")",
              comment: col.getComment()
      ])
    }
  }
  //JOptionPane.showMessageDialog(null, fields);
  return fields;
}

def javaName(str, capitalize) {
  def s = com.intellij.psi.codeStyle.NameUtil.splitNameIntoWords(str)
          .collect { Case.LOWER.apply(it).capitalize() }
          .join("")
          .replaceAll(/[^\p{javaJavaIdentifierPart}[_]]/, "_")
  capitalize || s.length() == 1? s : Case.LOWER.apply(s[0]) + s[1..-1]
}
