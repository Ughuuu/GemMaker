package scripts.example.app;

import com.gem.component.Script;

public class HolderScript extends Script {
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
    	i++;
    	//System.out.println(this.getClass().getName());
    }
}