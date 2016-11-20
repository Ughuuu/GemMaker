package data.scripts.com.example.app;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gem.component.ComponentPoint;
import com.gem.component.Script;

public class Test extends Script {
    ComponentPoint point;
    Vector2 center = new Vector2();
    float spd;

    @Override
    public void onInit() {
        point = holder.getComponent(ComponentPoint.class);
        Vector3 pct = point.getPosition();
    }

    @Override
    public void onUpdate(float delta) {
    }
}

