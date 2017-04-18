package com.gem.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.gem.component.ComponentCamera;
import com.gem.component.ComponentMaterial;
import com.gem.component.ComponentMesh;
import com.gem.component.ComponentPoint;
import com.gem.engine.Gem;
import com.gem.entity.Entity;

/**
 * @author Dragos
 * @hidden
 */
public class SystemDraw extends SystemBase {
    int cameraId;
    Matrix4 cam;

    public SystemDraw(Gem ng, SystemConfiguration conf) {
        super(ng, conf);
    }

    @Override
    public void onBeforeUpdate() {
    }

    @Override
    public void onUpdate(Entity ent) {
        ComponentCamera camera = Gem.goFindUpWithComponent(ent, ComponentCamera.class);
        if (camera == null) {
            return;
        }
        cam = camera.getCombined();

        Matrix4 model = getModel(ent);
        Mesh mesh = ent.getComponent(ComponentMesh.class).getMesh();
        if(mesh == null)
        	return;
        ShaderProgram prog = ent.getComponent(ComponentMaterial.class).getShader();

        //no need for depth...
        Gdx.gl.glDepthMask(false);

        //enable blending, for alpha
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        prog.begin();
        prog.setUniformMatrix("u_projView", cam);
        prog.setUniformMatrix("u_Model", model);

        //mesh.bind(prog);
        mesh.render(prog, ent.getComponent(ComponentMesh.class).getMeshType().getType());
        prog.end();
        //re-enable depth to reset states to their default
        Gdx.gl.glDepthMask(true);
    }

    private Matrix4 getModel(Entity ent) {
        Matrix4 pos = ent.getComponent(ComponentPoint.class).getMatrix();
        return pos;
    }
}