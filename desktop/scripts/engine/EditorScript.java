package scripts.engine;

import com.gem.component.*;

public class EditorScript extends Script {
    public int i=0;

    public int getI(){
        return i;
    }
    
    @Override
    public void onInit() {
        i=0;
    }

    @Override
    public void onUpdate(float delta) {
    	//System.out.println(gem.getAsset("engine/shaders/BasicShader"));
    }
}