package app.model
{
	import app.vo.Book;
	
	import mx.collections.ArrayCollection;
	
	public class BookModel
	{
		
		private var _list:ArrayCollection = null;
		
		
		public function BookModel() {
		}
		
		public function clearList():void {
			if (_list != null) {
				_list.removeAll();
			}
		}
		
		public function deleteBook(bookId:int):void {
			var i:int = 0;
			
			for (i=0; i<_list.length; i++) {
				var item:Book = (_list.getItemAt(i) as Book);
				if (item.bookid == bookId) {
					_list.removeItemAt(i);
					break;
				}
			}
		}
		
		public function updateBook(target:Book):void {
			var i:int = 0;
			
			for (i=0; i<_list.length; i++) {
				var item:Book = (_list.getItemAt(i) as Book);
				if (item.bookid == target.bookid) {
					item.author = target.author;
					item.title = target.title;
					break;
				}
			}
		}
		
		public function addBook(vo:Book):void {
			if (_list == null) {
				_list = new ArrayCollection();
			}
			_list.addItem(vo);
		}
		
		public function get list():ArrayCollection {
			return _list;
		}
	}
}