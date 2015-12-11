package menu;

public enum MenuImage{
	TITLE("assets/menu/StartScreen/title_BG.png"),
	NEW_GAME("assets/menu/StartScreen/new_game.png"),
	LOAD_GAME("assets/menu/StartScreen/load_game.png"),
    GAME_OVER("assets/menu/game_over.png");
	
	private String imageString;
	private MenuImage(String image){
		this.imageString = image;
	}
	public String image(){
		return imageString;
	}
}