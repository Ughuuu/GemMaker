package scripts.engine.editor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.gem.component.ComponentMesh;
import com.gem.component.Script;

public class Draw extends Script {
    ComponentMesh mesh;
    float[] verts;
    int idx = 0;

    @Override
    public void onInit() {
    	idx = 0;
		mesh = holder.getComponent(ComponentMesh.class);
		mesh.setVertLen(1024);
		verts = mesh.getVertices();
    }
    
    @Override
    public void onBeforeUpdate() {
		clearDraw();
    }

    @Override
    public void onUpdate(float delta) {
    }

    @Override
    public void onAfterUpdate(){
        mesh.setVertices(verts);
    } 

    public void drawLine(float x1, float y1, float x2, float y2, Color col) {
        verts[idx++] = x1;
        verts[idx++] = y1;
        verts[idx++] = col.toFloatBits();

        verts[idx++] = x2;
        verts[idx++] = y2;
        verts[idx++] = col.toFloatBits();
    }

    public void drawRectangle(Vector3 pos1, Vector3 pos2, Color col) {
        drawLine(pos1.x, pos1.y, pos1.x, pos2.y, col);
        drawLine(pos1.x, pos1.y, pos2.x, pos1.y, col);
        drawLine(pos2.x, pos2.y, pos2.x, pos1.y, col);
        drawLine(pos2.x, pos2.y, pos1.x, pos2.y, col);
    }

    public void drawX(Vector3 pos, float size) {
        drawLine(pos.x - size / 2, pos.y - size / 2, pos.x + size / 2, pos.y + size / 2, Color.WHITE);
        drawLine(pos.x - size / 2, pos.y + size / 2, pos.x + size / 2, pos.y - size / 2, Color.WHITE);
    }

    public void clearDraw() {
        idx = 0;
        for (int i = 0; i < verts.length; i++) {
            verts[i] = 0;
        }
    }
}