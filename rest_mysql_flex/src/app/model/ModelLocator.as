package app.model
{
	import com.adobe.cairngorm.model.ModelLocator;

	[Bindable]
	public class ModelLocator implements com.adobe.cairngorm.model.ModelLocator
	{
		private static var modelLocator:app.model.ModelLocator; 
		
		public var bookModel:BookModel = new BookModel();
		
		public static function getInstance():app.model.ModelLocator {
			if (modelLocator == null) {
				modelLocator = new app.model.ModelLocator(); 
			}
			return modelLocator; 
		}
		
		public function ModelLocator() {
			if (app.model.ModelLocator.modelLocator != null) {
				throw new Error("Must be only one modelLocator"); 
			}
		}
		
	}
}
		