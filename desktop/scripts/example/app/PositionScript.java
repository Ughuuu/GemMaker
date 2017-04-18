package scripts.example.app;

import com.badlogic.gdx.math.Vector3;
import com.gem.component.ComponentPoint;
import com.gem.component.Script;

public class PositionScript extends Script {
    ComponentPoint point;

    @Override
    public void onInit() {
        point = holder.getComponent(ComponentPoint.class);
    }

    @Override
    public void onUpdate(float delta) {
        if (point != null) {
            Vector3 p = point.getPosition();
            //p.x--;
            point.setPosition(p);
        }
    }
}

