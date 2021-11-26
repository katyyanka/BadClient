package animations;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Shake {
    private TranslateTransition tt;

    public Shake(Node node){
        tt = new TranslateTransition(Duration.millis(70), node);
        tt.setFromX(0f);
        tt.setByY(10f);
        tt.setCycleCount(4);
       /* tt.setAutoReverse(true);
        tt.setByY(-10f);
        tt.setCycleCount(4);*/
        tt.setAutoReverse(true); //чтоб возвращалось обратно

    }

    public  void playAnim(){
        tt.playFromStart();
    }
}
