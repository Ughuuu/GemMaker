package com.example.app;

import com.badlogic.gdx.math.Vector3;
import com.ngeen.component.ComponentPoint;
import com.ngeen.component.ComponentScript;
import com.ngeen.component.ComponentSprite;
import com.ngeen.component.ui.ComponentUIButton;
import com.ngeen.component.ui.ComponentUILabel;
import com.ngeen.component.ui.ComponentUIStage;
import com.ngeen.component.ui.ComponentUITable;
import com.ngeen.entity.Entity;
import com.ngeen.scene.Scene;

public class TestScene extends Scene {
    Entity ent;
    Entity output;

    @Override
    public void onInit() {
        doStuff();
    }

    @Override
    public void onUpdate(float delta) {
        if (output == null) {
            output = ng.getEntity("Label2");
            return;
        }
        try {
            // output.getComponent(ComponentUILabel.class).setText(Debugger.emptyBuffer());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void doStuff() {

        ng.getEntity("~UICAMERA");

        ent = CreateObject("Hello");
        ent.addComponent(ComponentSprite.class).setTexture("LoadScene/textures/ok.png");
        ent.addComponent(ComponentPoint.class).setPosition(new Vector3(-100, 0, 0)).setScale(1);

        Entity stage = CreateObject("Stage");
        stage.addComponent(ComponentPoint.class).setPosition(new Vector3(300, 0, 0));
        stage.addComponent(ComponentUIStage.class);
        stage.addComponent(ComponentScript.class).setScript(ExampleScript.class);
        // Entity enttt = ng.EntityBuilder.getByName("Stage");
        // System.out.println(enttt);

        Entity table = CreateObject("Table");
        table.addComponent(ComponentPoint.class).setPosition(new Vector3(200, 0, 0));
        table.addComponent(ComponentUITable.class);
        table.setParent("Stage");

        Entity widget = CreateObject("Widget");
        widget.addComponent(ComponentPoint.class).setPosition(new Vector3(100, 0, 0));
        widget.addComponent(ComponentUILabel.class);
        widget.setParent("Table");

        Entity button = CreateObject("Button");
        button.addComponent(ComponentPoint.class).setPosition(new Vector3(250, 250, 0));
        button.addComponent(ComponentUIButton.class);
        button.setParent("Stage");

        output = CreateObject("Label2");
        output.addComponent(ComponentPoint.class).setPosition(new Vector3(-250, -250, 0));
        output.addComponent(ComponentUILabel.class);
        output.setParent("Stage");

    }

    void startTestSuite() {
        // new TestSystemConfiguration(ng);
        // new TestAsset(ng);
        // new TestComponents(ng);
        // new TestEntity(ng);
    }
}
