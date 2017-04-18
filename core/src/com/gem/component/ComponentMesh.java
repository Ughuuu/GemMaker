package com.gem.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.gem.engine.Gem;
import com.gem.entity.ComponentSpokesman;
import com.gem.entity.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

public class ComponentMesh extends ComponentBase {
	@Getter
    private BoundingBox Box;
	@Getter
    private Mesh mesh;
    @Getter @Setter
    private MeshType meshType = MeshType.Triangles;
    private float[] vert;
    private short[] ind;
    @Getter
    private int vertLen = 0;
    @Getter
    private int indLen = 0;

    public ComponentMesh(Gem ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
        super(ng, ent, factory, _ComponentSpokesman);
    }

    public ComponentMesh setIndices(short[] indices) {
        mesh.setIndices(indices);
        return this;
    }

    public ComponentMesh setVertices(float[] verts) {
        mesh.setVertices(verts);
        return this;
    }

    public BoundingBox getBoundingBox() {
        try {
            mesh.calculateBoundingBox(Box);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Box;
    }

    @Override
    protected void notifyWithComponent(ComponentMaterial component) {
    	if(vert == null)
    		return;
    	vertLen = vert == null ? 0 : vert.length;
    	indLen = ind == null ? 0 : ind.length;
        mesh = gem.meshBuilder.makeMesh(component.getShader(), vertLen, indLen);
        if (ind != null) {
            setIndices(ind);
        }
        if (vert != null) {
            setVertices(vert);
        }
        ind = null;
        vert = null;
    }
    
    public ComponentMesh setVertLen(int len){
    	vertLen = len;
        mesh = gem.meshBuilder.makeMesh(owner.getComponent(ComponentMaterial.class).getShader(), vertLen, indLen);
        return this;
    }
    
    public ComponentMesh setIndLen(int len){
    	indLen = len;
        mesh = gem.meshBuilder.makeMesh(owner.getComponent(ComponentMaterial.class).getShader(), vertLen, indLen);
        return this;
    }

    @Override
    protected ComponentBase Load(Element element) throws Exception {
        try {
            meshType = MeshType.valueOf(element.get("MeshType"));
        } catch (Exception e) {
        }
        try {
            val indices = element.getChildByName("Indices").getText();
            if (indices != null) {
                val indicesArray = indices.split(", ");
                ind = new short[indicesArray.length];
                for (int i = 0; i < ind.length; i++) {
                    ind[i] = Short.parseShort(indicesArray[i]);
                }
            }

            val vertices = element.getChildByName("Vertices").getText();
            if (vertices != null) {
                val verticesArray = vertices.split(", ");
                vert = new float[verticesArray.length];
                for (int i = 0; i < vert.length; i++) {
                    vert[i] = Float.parseFloat(verticesArray[i]);
                }
            }
        } catch (Exception e) {
        	vert = null;
        }
        return this;
    }
    
    public float[] getVertices(){
        FloatBuffer vertices = mesh.getVerticesBuffer();
        vert = new float[mesh.getMaxVertices()];

        while (vertices.hasRemaining()) {
            vert[vertices.position()] = vertices.get();
        }
        return vert;
    }
    
    public short[] getIndices(){
        ShortBuffer indices = mesh.getIndicesBuffer();
        ind = new short[mesh.getMaxIndices()];
        while (indices.hasRemaining()) {
            ind[indices.position()] = indices.get();
        }
        return ind;
    }

    @Override
    protected void Save(XmlWriter element) throws Exception {
        ShortBuffer indices = mesh.getIndicesBuffer();
        FloatBuffer vertices = mesh.getVerticesBuffer();
        ind = new short[mesh.getMaxIndices()];
        vert = new float[mesh.getMaxVertices()];

        while (indices.hasRemaining()) {
            ind[indices.position()] = indices.get();
        }
        while (vertices.hasRemaining()) {
            vert[vertices.position()] = vertices.get();
        }

        element.element("Component").attribute("Type", type.getName())
                .element("Indices").text(Arrays.toString(ind).replace("[", "").replace("]", "")).pop()
                .element("Vertices").text(Arrays.toString(vert).replace("[", "").replace("]", "")).pop()
                .element("MeshType").text(meshType).pop()
                .pop();
        ind = null;
        vert = null;
    }

    @Override
    protected void visitComponent(ComponentBase component, ComponentFactory factory) {
        component.notifyWithComponent(this);
    }
    
    public static enum MeshType {
    	Points(GL20.GL_POINTS),
    	Lines(GL20.GL_LINES),
    	LineLoop(GL20.GL_LINE_LOOP),
    	LineStrip(GL20.GL_LINE_STRIP),
    	Triangles(GL20.GL_TRIANGLES),
    	TriangleStrip(GL20.GL_TRIANGLE_STRIP),
    	TriangleFan(GL20.GL_TRIANGLE_FAN);
        
    	@Getter
        private int type;
        
        MeshType(int type){
        	this.type = type;
        }
    }
}
