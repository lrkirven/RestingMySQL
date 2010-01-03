package app.vo
{
	import flash.xml.XMLDocument;
	import flash.xml.XMLNode;
	
	import mx.rpc.xml.SimpleXMLEncoder;
	
	public class Book
	{
		
		public var bookid:int = -1;
		public var author:String = null;
		public var title:String = null;
		
		public function deserialize(obj:Object):void {
			if (obj.hasOwnProperty("bookid")) {
				this.bookid = obj.bookid;
			}
			if (obj.hasOwnProperty("author")) {
				this.author = obj.author;
			}
			if (obj.hasOwnProperty("title")) {
				this.title = obj.title;
			}
		}
		
		public function serialize():XML {
			var qName:QName = new QName("app.vo.Book");
            var xmlDocument:XMLDocument = new XMLDocument();
            var encoder:SimpleXMLEncoder = new SimpleXMLEncoder(xmlDocument);
            var xmlNode:XMLNode = encoder.encodeValue(this, qName, xmlDocument);
            var xml:XML = new XML(xmlDocument.toString());
        	return xml;

		}
		
	}
}