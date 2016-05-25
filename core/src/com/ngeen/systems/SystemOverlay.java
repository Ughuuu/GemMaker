package com.ngeen.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.ngeen.action.CommandFactory;
import com.ngeen.component.ComponentCamera;
import com.ngeen.component.ComponentPoint;
import com.ngeen.engine.EngineInfo;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;
import com.ngeen.entity.XmlEntity;

import java.awt.event.KeyEvent;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Dragos
 * @composed 1 - 1 OverlaySelector
 */
public class SystemOverlay extends SystemBase implements GestureListener, InputProcessor {
    public static float _X1;
    public static float _X2;
    public static float _Y1;
    public static float _Y2;
    private final XmlEntity xmlFactory;
    private Set<Entity> _AllEntities = new TreeSet<Entity>(new Comparator<Entity>() {

        @Override
        public int compare(Entity o1, Entity o2) {
            return Integer.signum(o1.hashCode() - o2.hashCode());
        }
    });
    private Matrix4 _Comb;
    private boolean _DeleteSelected = false, _SelectAll = false, _WriteMode = false, _Shift = false, _Ctrl, _Alt;
    private String _EntityName = "null";
    private int _ModifyType = 0;
    private OverlaySelector _OverlaySelector;
    private Set<Entity> _Selected = new TreeSet<Entity>(new Comparator<Entity>() {

        @Override
        public int compare(Entity o1, Entity o2) {
            return Integer.signum(o1.hashCode() - o2.hashCode());
        }
    });
    private boolean _Selecting = false;
    private BoundingBox _Selection = new BoundingBox();
    private ShapeRenderer _ShapeRenderer;
    private SpriteBatch _SpriteBatch;
    private boolean reload;

    public SystemOverlay(Ngeen ng, SystemConfiguration conf, SpriteBatch batch, XmlEntity xml) {
        super(ng, conf);
        xmlFactory = xml;
        _ShapeRenderer = new ShapeRenderer();
        _OverlaySelector = new OverlaySelector(this, _ShapeRenderer, _Ng);
        _SpriteBatch = batch;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isPrintableChar(char c) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
        return (!Character.isISOControl(c)) && c != KeyEvent.CHAR_UNDEFINED && block != null
                && block != Character.UnicodeBlock.SPECIALS;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (Input.Keys.SHIFT_LEFT == keycode || Input.Keys.SHIFT_RIGHT == keycode) {
            _Shift = true;
        }
        if (Input.Keys.CONTROL_LEFT == keycode || Input.Keys.CONTROL_RIGHT == keycode) {
            _Ctrl = true;
        }
        if (Input.Keys.ALT_LEFT == keycode || Input.Keys.ALT_RIGHT == keycode) {
            _Alt = true;
        }
        if (Input.Keys.SPACE == keycode) {
            xmlFactory.Save();
        }
        if (Input.Keys.Z == keycode && _Ctrl) {
            CommandFactory.factory.undoAction();
        }
        if (Input.Keys.Y == keycode && _Ctrl) {
            CommandFactory.factory.redoAction();
        }
        if (Input.Keys.L == keycode && _Shift) {
            reload = true;
        }
        if (Input.Keys.BACKSPACE == keycode) {
            if (_EntityName.length() != 0)
                _EntityName = _EntityName.substring(0, _EntityName.length() - 1);
            _Selected.iterator().next().setName(_EntityName);
        }
        if (Input.Keys.FORWARD_DEL == keycode) {
            _DeleteSelected = true;
        }
        if (Input.Keys.F2 == keycode) {
            if (_Selected.size() == 0) {
                return false;
            }
            if (_WriteMode) {
                _WriteMode = false;
            } else {
                _WriteMode = true;
                _EntityName = _Selected.iterator().next().getName();
            }
        }
        if (Input.Keys.ENTER == keycode) {
            _WriteMode = false;
        }
        if (_WriteMode) {
            return false;
        }
        if (Input.Keys.C == keycode) {
            _Selected.clear();
            Entity ent = _Ng.EntityBuilder.makeEntity("NewEntity");
            ent.addComponent(ComponentPoint.class);
            _Selected.add(ent);
        }
        if (Input.Keys.A == keycode) {
            _SelectAll = true;
        }
        if (Input.Keys.S == keycode) {
            if (_ModifyType != 0)
                _ModifyType = 0;
            else
                _ModifyType = 3;
        }
        if (Input.Keys.R == keycode) {
            if (_ModifyType != 0)
                _ModifyType = 0;
            else
                _ModifyType = 2;
        }
        if (Input.Keys.T == keycode) {
            if (_ModifyType != 0)
                _ModifyType = 0;
            else
                _ModifyType = 1;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        if (_WriteMode) {
            if (isPrintableChar(character)) {
                _EntityName += character;
                _Selected.iterator().next().setName(_EntityName);
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (Input.Keys.SHIFT_LEFT == keycode || Input.Keys.SHIFT_RIGHT == keycode) {
            _Shift = false;
        }
        if (Input.Keys.CONTROL_LEFT == keycode || Input.Keys.CONTROL_RIGHT == keycode) {
            _Ctrl = false;
        }
        if (Input.Keys.ALT_LEFT == keycode || Input.Keys.ALT_RIGHT == keycode) {
            _Alt = false;
        }
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public void onAfterUpdate() {
        _ShapeRenderer.end();

        _SpriteBatch.begin();
        _SpriteBatch.setProjectionMatrix(_Comb);
        BitmapFont font = (BitmapFont) _Ng.Loader.getAsset("engine/fonts/impact.fnt").getData();
        font.getData().setScale(0.2f);
        // font.setColor(1, 1, 1, 0.5f);
        for (Entity ent : _AllEntities) {
            Matrix4 onlyPos = new Matrix4(ent.getComponent(ComponentPoint.class).getPosition(), new Quaternion(),
                    new Vector3(1, 1, 1));
            _SpriteBatch.setTransformMatrix(onlyPos);
            font.draw(_SpriteBatch, ent.getName(), 0, 0);
        }
        _SpriteBatch.end();
        _AllEntities.clear();

        if (reload) {
            resetState();
            xmlFactory.Load();
        }
    }

    @Override
    public void onBeforeUpdate() {
        if (_SelectAll) {
            SelectAll();
        }
        if (_DeleteSelected) {
            DeleteAll();
        }
        _Comb = _Ng.EntityBuilder.getByName("~CAMERA").getComponent(ComponentCamera.class).Camera.combined;
        _ShapeRenderer.begin(ShapeType.Line);
        computeSelection();
        if (_ModifyType != 0) {
            MoveAll();
            return;
        }
        if (_Selecting && !_Shift)
            _Selected.clear();
        drawSelection();
    }

    @Override
    public void onUpdate(Entity ent) {
        _AllEntities.add(ent);
        if (_Selecting)
            _OverlaySelector.Select(ent, _Selected, _Selection);

        _OverlaySelector.Overlay(ent, _Comb, _Selected.contains(ent));
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        ComponentCamera cam = _Ng.EntityBuilder.getByName("~CAMERA").getComponent(ComponentCamera.class);
        float ratio = cam.Camera.viewportWidth / cam.Camera.viewportHeight;
        cam.Camera.viewportWidth += ratio * amount * 20;
        cam.Camera.viewportHeight += amount * 20;
        EngineInfo.Width = cam.Camera.viewportWidth;
        EngineInfo.Height = cam.Camera.viewportHeight;
        cam.Camera.update();
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        _WriteMode = false;
        _ModifyType = 0;
        if (button != Input.Buttons.LEFT) {
            return false;
        }
        _Selecting = true;
        _X1 = screenX;
        _Y1 = screenY;
        _X2 = _X1;
        _Y2 = _Y1;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        _Selecting = true;
        _X2 = screenX;
        _Y2 = screenY;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button != Input.Buttons.LEFT) {
            return false;
        }
        _Selecting = false;
        _X1 = 0;
        _Y1 = 0;
        _X2 = 0;
        _Y2 = 0;
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        // TODO Auto-generated method stub
        return false;
    }

    private void computeSelection() {
        Entity ent = _Ng.EntityBuilder.getByName("~UICAMERA");
        ComponentCamera cam = ent.getComponent(ComponentCamera.class);

        Entity ent2 = _Ng.EntityBuilder.getByName("~CAMERA");
        ComponentCamera cam2 = ent2.getComponent(ComponentCamera.class);

        Matrix4 comb = cam.Camera.combined;
        _Selection = new BoundingBox(new Vector3(_X1, EngineInfo.ScreenHeight - _Y1, -10),
                new Vector3(_X2, EngineInfo.ScreenHeight - _Y2, 10));
        _Selection.mul(new Matrix4().translate(new Vector3(-cam.Camera.position.x, -cam.Camera.position.y, 0)));
        _Selection
                .mul(new Matrix4(new Vector3(), new Quaternion(), new Vector3(EngineInfo.Width / EngineInfo.ScreenWidth,
                        EngineInfo.Height / EngineInfo.ScreenHeight, 1)));
    }

    private void drawSelection() {
        Entity ent = _Ng.EntityBuilder.getByName("~UICAMERA");
        ComponentCamera cam = ent.getComponent(ComponentCamera.class);
        Matrix4 comb = cam.Camera.combined;
        _ShapeRenderer.setProjectionMatrix(comb);
        _ShapeRenderer.rect(_X1, EngineInfo.ScreenHeight - _Y1, _X2 - _X1, -_Y2 + _Y1);
    }

    private void MoveAll() {
        for (Entity ent : _Selected) {
            if (ent.getParent() != null && _Selected.contains(ent.getParent()))
                continue;
            float deltaX = Gdx.input.getDeltaX(), deltaY = Gdx.input.getDeltaY();
            Vector3 modif = new Vector3(deltaX, deltaY, 0);
            modif.mul(new Matrix4(new Vector3(), new Quaternion(), new Vector3(
                    EngineInfo.Width / EngineInfo.ScreenWidth, EngineInfo.Height / EngineInfo.ScreenHeight, 1)));
            modif.y *= -1;
            switch (_ModifyType) {
                case 1:
                    Vector3 pos = new Vector3(ent.getComponent(ComponentPoint.class).getPosition());
                    pos.add(modif);
                    ent.getComponent(ComponentPoint.class).setPosition(pos);
                    break;
                case 2:
                    Vector3 rot = new Vector3(ent.getComponent(ComponentPoint.class).getRotation());
                    rot.add(0, 0, (deltaX - deltaY));
                    ent.getComponent(ComponentPoint.class).setRotation(rot);
                    break;
                case 3:
                    Vector3 sc = new Vector3(ent.getComponent(ComponentPoint.class).getScale());
                    modif.y *= -1;
                    sc.add(modif.scl(1 / 100.0f));
                    ent.getComponent(ComponentPoint.class).setScale(sc);
                    break;
            }
        }
        _X1 = _X2;
        _Y1 = _Y2;
    }

    void DeleteAll() {
        for (Entity ent : _Selected) {
            ent.remove();
        }
        _Selected.clear();
        _DeleteSelected = false;
    }

    void resetState() {
        reload = false;
        _ModifyType = 0;
        _DeleteSelected = false;
        _SelectAll = false;
        _WriteMode = false;
        _EntityName = "null";
    }

    void SelectAll() {
        _Selected.clear();
        for (Entity ent : _Ng.EntityBuilder.getEntities()) {
            if (ent.hasComponent(ComponentPoint.class) && !ent.hasComponent(ComponentCamera.class))
                _Selected.add(ent);
        }
        _SelectAll = false;
    }
}
