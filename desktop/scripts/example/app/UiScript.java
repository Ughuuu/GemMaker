package scripts.example.app;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gem.component.ComponentPoint;
import com.gem.component.Script;
import com.gem.component.ui.ComponentUILabel;
import com.gem.component.ComponentScript;

public class UiScript extends Script {
    ComponentUILabel uiLabel;

    @Override
    public void onInit() {
        uiLabel = holder.getComponent(ComponentUILabel.class);
    }

    int getIFromOtherScript(){
    	Script s = gem.getEntity("Stage").getComponent(ComponentScript.class).getScript();
    	try{
	    	return (int)s.getClass().getField("i").get(s);
    	}catch(Exception e){    		
    	}
    	return 0;
    }
    
    @Override
    public void onUpdate(float delta) {  
	    if(uiLabel != null){
	    	uiLabel.setText("Hey " + getIFromOtherScript());
	    }
    }
}