package sbt.datatype

import org.specs2._
import SchemaExample._
import NewSchema._

class SchemaSpec extends Specification {
  def is = s2"""
    This is a specification for data type spec.

    ProtocolSchema.parse should
      parse                                      $e1

    Schema.parse should
      parse empty Schemas                        $emptySchema
      parse schema with only a namespace         $onlyNamespaceSchema
      parse complete example                     $complete

    Definition.parse should
      parse protocol                             $definitionParseProtocol
      parse record                               $definitionParseRecord
      parse enumeration                          $definitionParseEnumeration
      throw an error on invalid definition kind  $definitionParseInvalidDefinitionKind

    Protocol.parse should
      parse simple protocol                      $protocolParseSimple
      parse protocol with one child              $protocolParseOneChild
      parse nested protocols                     $protocolParseNested

    Record.parse should
      parse simple record                        $recordParseSimple

    Enumeration.parse should
      parse simple enumeration                   $enumerationParseSimple

    Field.parse should
      parse                                      $fieldParse

    TpeRef.apply should
      parse simple types                         $tpeRefParseSimple
      parse lazy types                           $tpeRefParseLazy
      parse array types                          $tpeRefParseArray
      parse lazy array types                     $tpeRefParseLazyArray
  """

  def e1 = {
    val s = ProtocolSchema.parse(basicSchema)
    s.namespace must_== "com.example"
  }

  def emptySchema = {
    val s = Schema parse emptySchemaExample
    s.namespace must_== None
    s.definitions must_== Nil
  }

  def onlyNamespaceSchema = {
    val s = Schema parse onlyNamespaceSchemaExample
    s.namespace must_== Some("com.example")
    s.definitions must_== Nil
  }

  def complete = {
    val s = Schema parse completeExample
    s.namespace must_== "com.example"
    s.definitions must haveSize(3)
  }

  def definitionParseProtocol = {
    Definition parse emptyProtocolExample match {
      case Protocol(name, doc, fields, children) =>
        name must_== "emptyProtocolExample"
        doc must_== None
        fields must haveSize(0)
        children must haveSize(0)

      case _ =>
        true must_== false
    }
  }

  def definitionParseRecord = {
    Definition parse emptyRecordExample match {
      case Record(name, doc, fields) =>
        name must_== "emptyRecordExample"
        doc must_== None
        fields must haveSize(0)

      case _ =>
        true must_== false
    }
  }

  def definitionParseEnumeration = {
    Definition parse emptyEnumerationExample match {
      case Enumeration(name, doc, values) =>
        name must_== "emptyEnumerationExample"
        doc must_== None
        values must haveSize(0)

      case _ =>
        true must_== false
    }
  }

  def definitionParseInvalidDefinitionKind =
    Definition parse invalidDefinitionKindExample must throwA[RuntimeException]

  def protocolParseSimple = {
    Protocol parse simpleProtocolExample match {
      case Protocol(name, doc, fields, children) =>
        name must_== "simpleProtocolExample"
        doc must_== Some("example of simple protocol")
        fields must haveSize(1)
        fields(0) must_== Field("field", None, TpeRef("type", false, false), Field.emptyVersion, None)
        children must haveSize(0)
    }
  }

  def protocolParseOneChild = {
    Protocol parse oneChildProtocolExample match {
      case Protocol(name, doc, fields, children) =>
        name must_== "oneChildProtocolExample"
        doc must_== Some("example of protocol")
        fields must haveSize(0)
        children must haveSize(1)
        children(0) must_== Record("childRecord", None, Nil)
    }
  }

  def protocolParseNested = {
    Protocol parse nestedProtocolExample match {
      case Protocol(name, doc, fields, children) =>
        name must_== "nestedProtocolExample"
        doc must_== Some("example of nested protocols")
        fields must haveSize(0)
        children must haveSize(1)
        children(0) must_== Protocol("nestedProtocol", None, Nil, Nil)
    }
  }

  def recordParseSimple = {
    Record parse simpleRecordExample match {
      case Record(name, doc, fields) =>
        name must_== "simpleRecordExample"
        doc must_== Some("Example of simple record")
        fields must haveSize(1)
        fields(0) must_== Field("field", None, TpeRef("type", false, false), Field.emptyVersion, None)
    }
  }

  def enumerationParseSimple = {
    Enumeration parse simpleEnumerationExample match {
      case Enumeration(name, doc, values) =>
        name must_== "simpleEnumerationExample"
        doc must_== Some("Example of simple enumeration")
        values must haveSize(2)
        values(0) must_== EnumerationValue("first", Some("First type"))
        values(1) must_== EnumerationValue("second", None)
    }
  }

  def fieldParse = {
    Field parse fieldExample match {
      case Field(name, doc, tpe, since, default) =>
        name must_== "fieldExample"
        doc must_== "Example of field"
        tpe must_== TpeRef("type", false, false)
        since must_== VersionNumber("1.0.0")
        default must_== Some("2 + 2")
    }

  }

  def tpeRefParseSimple = {
    TpeRef apply simpleTpeRefExample match {
      case TpeRef(name, lzy, repeated) =>
        name must_== "simpleTpeRefExample"
        lzy must_== false
        repeated must_== false
    }
  }

  def tpeRefParseLazy = {
    TpeRef apply lazyTpeRefExample match {
      case TpeRef(name, lzy, repeated) =>
        name must_== "lazyTpeRefExample"
        lzy must_== true
        repeated must_== false
    }
  }

  def tpeRefParseArray = {
    TpeRef apply arrayTpeRefExample match {
      case TpeRef(name, lzy, repeated) =>
        name must_== "arrayTpeRefExample"
        lzy must_== false
        repeated must_== true
    }
  }

  def tpeRefParseLazyArray = {
    TpeRef apply lazyArrayTpeRefExample match {
      case TpeRef(name, lzy, repeated) =>
        name must_== "lazyArrayTpeRefExample"
        lzy must_== true
        repeated must_== true
    }
  }

}
