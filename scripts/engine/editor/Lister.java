package scripts.engine.editor;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.badlogic.gdx.math.Vector3;
import com.gem.component.ComponentPoint;
import com.gem.component.Script;
import com.gem.component.ui.ComponentUILabel;
import com.gem.entity.Entity;

public class Lister extends Script {
	private Map<String, Integer> entityMap = new TreeMap<String, Integer>();
	private ComponentUILabel label; 
	private ComponentPoint point;
	private ComponentPoint parent;
	
    @Override
    public void onInit() {
		parent = holder.getParent().getComponent(ComponentPoint.class);
		point = holder.getComponent(ComponentPoint.class);
    	label = holder.getComponent(ComponentUILabel.class);
    }
    
    private void updateLabel(){
    	StringBuilder text = new StringBuilder();
    	for(Map.Entry<String, Integer> entry : entityMap.entrySet()){
    		text.append(entry.getKey() + "\n");
    	}
    	label.setText(text.toString());
    }

    private void removeEnt(int id){
    	updateLabel();
    }
    
    private void addEnt(Entity ent){
    	updateLabel();
    }
    
    @Override
    public void onUpdate(float delta) {
		Vector3 parentPos = parent.getPosition();
		point.setRelativePosition(new Vector3(-parentPos.x, -parentPos.y, 0));
    	List<Entity> allEntities = gem.entityBuilder.getEntities();
    	Iterator<Map.Entry<String,Integer>> iter = entityMap.entrySet().iterator();
    	while (iter.hasNext()) {
    	    Map.Entry<String,Integer> entry = iter.next();
    		if(!allEntities.stream().anyMatch(t -> t.getName() == entry.getKey())){
    			removeEnt(entry.getValue());
    	        iter.remove(); 
    	    }
    	}
    	for(Entity ent : allEntities){ 
    		if(entityMap.get(ent.getName()) == null && ent.getName().indexOf('~') == -1){
    	    	entityMap.put(ent.getName(), ent.getId());
    			addEnt(ent);
    		}
    	}
    }
}