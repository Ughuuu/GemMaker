package com.ngeen.ui;

import java.util.HashMap;
import java.util.Map;

import sun.font.CreatedFontTracker;

import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ngeen.components.AnimationComponent;
import com.ngeen.components.ButtonComponent;
import com.ngeen.components.CameraComponent;
import com.ngeen.components.GroupComponent;
import com.ngeen.components.MusicComponent;
import com.ngeen.components.ParticleComponent;
import com.ngeen.components.PhysicsComponent;
import com.ngeen.components.ResourceComponent;
import com.ngeen.components.ScriptComponent;
import com.ngeen.components.SoundComponent;
import com.ngeen.components.TagComponent;
import com.ngeen.components.TextComponent;
import com.ngeen.components.TextureComponent;
import com.ngeen.components.TransformComponent;
import com.ngeen.holder.Constant;
import com.ngeen.holder.Ngeen;
import com.ngeen.ui.creators.Creator;
import com.ngeen.ui.creators.CreatorAnimation;
import com.ngeen.ui.creators.CreatorButton;
import com.ngeen.ui.creators.CreatorCamera;
import com.ngeen.ui.creators.CreatorGroup;
import com.ngeen.ui.creators.CreatorMusic;
import com.ngeen.ui.creators.CreatorParticle;
import com.ngeen.ui.creators.CreatorPhysics;
import com.ngeen.ui.creators.CreatorResource;
import com.ngeen.ui.creators.CreatorScript;
import com.ngeen.ui.creators.CreatorSound;
import com.ngeen.ui.creators.CreatorTag;
import com.ngeen.ui.creators.CreatorText;
import com.ngeen.ui.creators.CreatorTexture;
import com.ngeen.ui.creators.CreatorTransform;

public class Interface extends Stage {
	public static Skin skin;
	public static TextureAtlas buttons;
	public int w, h, uniqueId = 0;
	public static TextArea debug;
	public static ScrollPane debugPane;
	public static ScrollPane lookPane;
	public static ImageTextButtonStyle entityStyle = new ImageTextButtonStyle();
	public static Map<Integer, ImageTextButton> entityMap = new HashMap<Integer, ImageTextButton>();
	public static float size = 4.5f, icons = 64f;
	public static int selected = -1;
	public static Map<String, Entity> sel = new HashMap<String, Entity>();
	public static boolean assetSel = false;

	public static Table entities, right, top, bottom, left, components, assets;

	public static Ngeen ng;

	public static void reselect(){
		int id = selected;
		changeEntity(ng.getById(id));
		//select(ng.getById(id));
	}
	
	public static void select(Entity e) {
		if(!Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)){
			for(Entity ent : sel.values()){
				entityMap.get(ent.id).setChecked(false);
				//deselect(ent);
			}
			sel.clear();
		}
		sel.put(e.getComponent(TagComponent.class).name, e);
		selected = e.id;
		AnimationComponent anim = e.getComponent(AnimationComponent.class);
		CreatorAnimation.addAnimation();
		if (anim != null) {
			CreatorAnimation.animationIndex.setText(anim.index + "");
			CreatorAnimation.animationPage.setText(anim.page);
			CreatorAnimation.animationResource
					.setText(ng.getById(anim.resource_index).getComponent(TagComponent.class).name);
		} else {
			CreatorAnimation.removeAnimation();
		}
		ButtonComponent butc = e.getComponent(ButtonComponent.class);
		CreatorButton.addButton();
		if (butc != null) {
			CreatorButton.buttonState.setText(butc.state + "");
			CreatorButton.buttonStates.setText(butc.states + "");
		} else {
			CreatorButton.removeButton();
		}
		CameraComponent camc = e.getComponent(CameraComponent.class);
		CreatorCamera.addCamera();
		if (camc != null) {
			CreatorCamera.cameraHeight.setText(camc.camera.viewportHeight + "");
			CreatorCamera.cameraWidth.setText(camc.camera.viewportWidth + "");
		} else {
			CreatorCamera.removeCamera();
		}
		GroupComponent groupc = e.getComponent(GroupComponent.class);
		CreatorGroup.addGroup();
		if (groupc != null) {
			CreatorGroup.groupName.setText(groupc.name);
		} else {
			CreatorGroup.removeGroup();
		}
		MusicComponent musicc = e.getComponent(MusicComponent.class);
		CreatorMusic.addMusic();
		if (musicc != null) {
			CreatorMusic.musicResource.setText(ng.getById(musicc.resource_index).getComponent(TagComponent.class).name);
		} else {
			CreatorMusic.removeMusic();
		}
		ParticleComponent partc = e.getComponent(ParticleComponent.class);
		CreatorParticle.addParticle();
		if (partc != null) {
			CreatorParticle.partDraw.setChecked(partc.draw);
			CreatorParticle.partResource
					.setText(ng.getById(partc.resource_index).getComponent(TagComponent.class).name);
		} else {
			CreatorParticle.removeParticle();
		}
		PhysicsComponent physc = e.getComponent(PhysicsComponent.class);
		CreatorPhysics.addPhysics();
		if (physc != null) {
		} else {
			CreatorPhysics.removePhysics();
		}
		ResourceComponent resc = e.getComponent(ResourceComponent.class);
		CreatorResource.addResource();
		if (resc != null) {
			CreatorResource.resourceType.setText(resc.resource.getClass().getSimpleName());
		} else {
			CreatorResource.removeResource();
		}
		ScriptComponent scriptc = e.getComponent(ScriptComponent.class);
		CreatorScript.addScript();
		if (scriptc != null) {
			CreatorScript.scriptScript.setText(scriptc.script.getClass().getSimpleName());
			CreatorScript.scriptEnable.setChecked(scriptc.script.active);
		} else {
			CreatorScript.removeScript();
		}
		SoundComponent soundc = e.getComponent(SoundComponent.class);
		CreatorSound.addSound();
		if (soundc != null) {
			CreatorSound.soundResource.setText(ng.getById(soundc.resource_index).getComponent(TagComponent.class).name);
		} else {
			CreatorSound.removeSound();
		}
		TagComponent tagc = e.getComponent(TagComponent.class);
		CreatorTag.addTag();
		if (tagc != null) {
			CreatorTag.tagName.setText(tagc.name);
		} else {
			CreatorTag.removeTag();
		}
		TextComponent textc = e.getComponent(TextComponent.class);
		CreatorText.addText();
		if (textc != null) {
			CreatorText.textDraw.setChecked(textc.draw);
			CreatorText.textResource.setText(ng.getById(textc.resource_index).getComponent(TagComponent.class).name);
			CreatorText.textText.setText(textc.text);
		} else {
			CreatorText.removeText();
		}
		TextureComponent texturec = e.getComponent(TextureComponent.class);
		CreatorTexture.addTexture();
		if (texturec != null) {
			CreatorTexture.textureResource
					.setText(ng.getById(texturec.resource_index).getComponent(TagComponent.class).name);
			CreatorTexture.textureDraw.setChecked(texturec.draw);
		} else {
			CreatorTexture.removeTexture();
		}
		TransformComponent transformc = e.getComponent(TransformComponent.class);
		CreatorTransform.addTransform();
		if (transformc != null) {
			CreatorTransform.transformAngle.setText(transformc.angle + "");
			CreatorTransform.transformDepth.setText(transformc.z + "");
			CreatorTransform.transformPositionX.setText(transformc.position.x + "");
			CreatorTransform.transformPositionY.setText(transformc.position.y + "");
			CreatorTransform.transformScaleX.setText(transformc.scale.x + "");
			CreatorTransform.transformScaleY.setText(transformc.scale.y + "");
		} else {
			CreatorTransform.removeTransform();
		}
	}

	public static void deselect(Entity e) {
		sel.remove(e.getComponent(TagComponent.class).name);
		selected = -1;
		CreatorAnimation.removeAnimation();
		CreatorButton.removeButton();
		CreatorCamera.removeCamera();
		CreatorGroup.removeGroup();
		CreatorMusic.removeMusic();
		CreatorParticle.removeParticle();
		CreatorPhysics.removePhysics();
		CreatorResource.removeResource();
		CreatorScript.removeScript();
		CreatorSound.removeSound();
		CreatorTag.removeTag();
		CreatorText.removeText();
		CreatorTexture.removeTexture();
		CreatorTransform.removeTransform();

		CreatorAnimation.animationNew.remove();
		CreatorButton.buttonNew.remove();
		CreatorCamera.cameraNew.remove();
		CreatorMusic.musicNew.remove();
		CreatorParticle.particleNew.remove();
		CreatorPhysics.physicsNew.remove();
		CreatorScript.scriptNew.remove();
		CreatorSound.soundNew.remove();
		CreatorText.textNew.remove();
		CreatorTexture.textureNew.remove();
		CreatorTransform.transformNew.remove();
	}

	public static void addEntity(final Entity e) {
		if (entityMap.get(e.id) == null) {
			select(e);
			deselect(e);
			println("Entity " + e.getId() + ": " + e.getComponent(TagComponent.class).name + " added.");
			final ImageTextButton ent = new ImageTextButton(e.getComponent(TagComponent.class).name, entityStyle);
			ent.setName("" + e.id);
			ent.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if(!ent.isChecked())
						deselect(e);
					else
						select(ng.getById(Integer.parseInt(ent.getName().toString())));
				};
			});
			entityMap.put(e.id, ent);
			if (e.getComponent(ResourceComponent.class) != null) {
				assets.add(ent).row();
			} else {
				entities.add(ent).row();
			}
		} else {
			changeEntity(e);
		}
	}

	public static void changeEntity(Entity e) {
		if (entityMap.get(e.id) != null) {
			entityMap.get(e.id).setText(e.getComponent(TagComponent.class).name);
		}
	}

	public static void deleteEntity(Entity e) {
		if (entityMap.get(e.id) != null) {
			if (e.id == selected) {
				deselect(e);
			}
			println("Entity " + e.getId() + ": " + e.getComponent(TagComponent.class).name + " deleted.");
			ImageTextButton but = entityMap.remove(e.id);
			but.remove();
		}
	}

	public static void notifyDebug() {
		debug.setPrefRows(50);
		// debug.setPrefRows(debug.getText().split("\n").length);
		debugPane.layout();
	}

	public static void print(String line) {
		debug.appendText(line);
		if (Constant.OUTPUT_OUT == true)
			System.out.println(line);
		notifyDebug();
	}

	public static void println(String line) {
		debug.appendText(line + "\n");
		if (Constant.OUTPUT_OUT == true)
			System.out.println(line);
		notifyDebug();
	}

	public Interface(Ngeen ng) {
		super(new ScreenViewport());
		this.ng = ng;
		init();
	}

	void init() {
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		skin = new Skin(Gdx.files.internal("debug/darkui.json"));
		skin.getFont("default-font").getData().setScale(size * 0.1f);
		buttons = new TextureAtlas("debug/def_but.atlas");

		Creator.buttons = buttons;
		Creator.skin = skin;
		Creator.w = w;
		Creator.h = h;

		entityStyle.up = new TextureRegionDrawable(buttons.findRegion("but", 0));
		entityStyle.checked = new TextureRegionDrawable(buttons.findRegion("but", 1));
		entityStyle.font = skin.getFont("default-font");

		top = createTopMenu();
		bottom = createBottomMenu();
		left = createLeftMenu();
		right = createRightMenu();

		this.addActor(left);
		this.addActor(top);
		this.addActor(right);
		this.addActor(bottom);
		this.setDebugAll(true);
	}

	public void changeDebug() {

	}

	Table createBottomMenu() {
		Table table = new Table(skin);

		final ImageButton but = new ImageButton(new TextureRegionDrawable(buttons.findRegion("expand", 0)),
				new TextureRegionDrawable(buttons.findRegion("expand", 1)));

		debug = new TextArea("This is ngeen(all lowercase) version 1.1.\n", skin);

		debugPane = new ScrollPane(debug, skin);
		debugPane.setScrollbarsOnTop(false);
		debugPane.setFadeScrollBars(false);
		debugPane.setCancelTouchFocus(false);

		table.add(debugPane).expand().fill();
		table.add(but);

		table.setSize(w, icons);
		table.setPosition(0, 0);

		// expand
		but.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (bottom.getHeight() == 250) {
					bottom.setSize(w, icons);
				} else
					bottom.setSize(w, 250);
			};
		});

		table.setBackground("square");
		return table;
	}

	Table createLeftMenu() {
		final Table table = new Table(skin);

		final ImageButton but = new ImageButton(new TextureRegionDrawable(buttons.findRegion("expand", 0)),
				new TextureRegionDrawable(buttons.findRegion("expand", 1)));

		final ImageButton switchb = new ImageButton(new TextureRegionDrawable(buttons.findRegion("report", 0)),
				new TextureRegionDrawable(buttons.findRegion("report", 1)));

		entities = new Table(skin);
		assets = new Table(skin);
		final ScrollPane pane1 = new ScrollPane(entities, skin);
		pane1.setCancelTouchFocus(false);
		final ScrollPane pane2 = new ScrollPane(assets, skin);
		pane2.setCancelTouchFocus(false);

		table.add(switchb).row();
		table.add(pane1).expand().fill();
		table.add(but);

		table.setSize(icons, h - icons * 2);
		table.setPosition(0, icons);

		// expand
		but.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (left.getWidth() == icons) {
					left.setSize(250, h - icons * 2);
				} else
					left.setSize(icons, h - icons * 2);
			};
		});

		// switch
		switchb.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				assetSel = !assetSel;
				if (assetSel) {
					final Table adder = new Table(skin);
					adder.add(pane2).expand().fill();
					table.getCells().get(1).setActor(adder);
				} else {
					final Table adder = new Table(skin);
					adder.add(pane1).expand().fill();
					table.getCells().get(1).setActor(adder);
				}
			};
		});

		table.setBackground("square");
		return table;
	}

	Table createTopMenu() {
		Table table = new Table(skin);
		final ImageButton[] but = new ImageButton[7];
		final CheckBox[] box = new CheckBox[3];
		final CheckBoxStyle[] boxStyle = new CheckBoxStyle[3];
		boxStyle[0] = new CheckBoxStyle(new TextureRegionDrawable(buttons.findRegion("pause", 0)),
				new TextureRegionDrawable(buttons.findRegion("pause", 1)), new BitmapFont(), new Color());
		boxStyle[1] = new CheckBoxStyle(new TextureRegionDrawable(buttons.findRegion("play", 0)),
				new TextureRegionDrawable(buttons.findRegion("play", 1)), new BitmapFont(), new Color());
		boxStyle[2] = new CheckBoxStyle(new TextureRegionDrawable(buttons.findRegion("mute", 0)),
				new TextureRegionDrawable(buttons.findRegion("mute", 1)), new BitmapFont(), new Color());
		box[0] = new CheckBox("", boxStyle[0]);
		box[1] = new CheckBox("", boxStyle[1]);
		box[2] = new CheckBox("", boxStyle[2]);

		but[0] = new ImageButton(new TextureRegionDrawable(buttons.findRegion("new", 0)),
				new TextureRegionDrawable(buttons.findRegion("new", 1)));
		but[1] = new ImageButton(new TextureRegionDrawable(buttons.findRegion("zoomin", 0)),
				new TextureRegionDrawable(buttons.findRegion("zoomin", 1)));
		but[2] = new ImageButton(new TextureRegionDrawable(buttons.findRegion("zoomout", 0)),
				new TextureRegionDrawable(buttons.findRegion("zoomout", 1)));
		TextureRegion t = new TextureRegion(buttons.findRegion("fast", 0));
		TextureRegion t1 = new TextureRegion(buttons.findRegion("fast", 1));
		t.flip(true, false);
		t1.flip(true, false);
		but[3] = new ImageButton(new TextureRegionDrawable(t), new TextureRegionDrawable(t1));
		but[4] = new ImageButton(new TextureRegionDrawable(buttons.findRegion("restart", 0)),
				new TextureRegionDrawable(buttons.findRegion("restart", 1)));
		t = buttons.findRegion("fast", 0);
		t1 = buttons.findRegion("fast", 1);
		but[5] = new ImageButton(new TextureRegionDrawable(t), new TextureRegionDrawable(t1));
		but[6] = new ImageButton(new TextureRegionDrawable(buttons.findRegion("exit", 0)),
				new TextureRegionDrawable(buttons.findRegion("exit", 1)));

		table.add(but[0]).expand();
		table.add(but[1]).expand();
		table.add(but[2]).expand();
		table.add(but[3]).expand();
		table.add(but[4]).expand();
		table.add(box[0]).expand();
		table.add(box[1]).expand();
		table.add(but[5]).expand();
		table.add(box[2]).expand();
		table.add(but[6]).expand();

		// add
		but[0].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ng.entityHelper.createPositional("n" + uniqueId, "default");
				uniqueId++;
			};
		});
		// zoom in
		but[1].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Constant.ZOOM -= 0.2f;
				ng.zoom();
			};
		});
		// zoom out
		but[2].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Constant.ZOOM += 0.2f;
				ng.zoom();
			};
		});
		// slow down
		but[3].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Constant.GAME_SPEED -= 0.5f * Constant.GAME_SPEED;
			};
		});
		// restart
		but[4].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ng.restart();
			};
		});
		// pause
		box[0].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Constant.GAME_STATE = false;
				if (!box[0].isChecked()) {
					box[0].setChecked(true);
				}
				box[1].setChecked(!box[0].isChecked());
			};
		});
		// play
		box[1].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Constant.GAME_STATE = true;
				if (!box[1].isChecked()) {
					box[1].setChecked(true);
				}
				box[0].setChecked(!box[1].isChecked());
			};
		});
		// fast
		but[5].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Constant.GAME_SPEED += 0.5f * Constant.GAME_SPEED;
			};
		});
		// mute
		box[2].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Constant.VOLUME = !Constant.VOLUME;
			};
		});
		// hide
		but[6].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			};
		});
		table.setSize(w, icons);
		table.setPosition(0, h - icons);
		table.setBackground("square");
		return table;
	}

	Table createRightMenu() {
		Table table = new Table(skin);

		final ImageButton but = new ImageButton(new TextureRegionDrawable(buttons.findRegion("expand", 0)),
				new TextureRegionDrawable(buttons.findRegion("expand", 1)));

		components = new Table(skin);

		CreatorAnimation.createAnimation();
		CreatorButton.createButton();
		CreatorCamera.createCamera();
		CreatorGroup.createGroup();
		CreatorMusic.createMusic();
		CreatorParticle.createParticle();
		CreatorPhysics.createPhysics();
		CreatorResource.createResource();
		CreatorScript.createScript();
		CreatorSound.createSound();
		CreatorTag.createTag();
		CreatorText.createText();
		CreatorTexture.createTexture();
		CreatorTransform.createTransform();

		components.add(Creator.tag).fillX().expandX();
		components.row();
		components.add(Creator.group).fillX().expandX();
		components.row();
		components.add(Creator.transform).fillX().expandX();
		components.row();
		components.add(Creator.texture).fillX().expandX();
		components.row();
		components.add(Creator.physics).fillX().expandX();
		components.row();
		components.add(Creator.particle).fillX().expandX();
		components.row();
		components.add(Creator.animation).fillX().expandX();
		components.row();
		components.add(Creator.button).fillX().expandX();
		components.row();
		components.add(Creator.text).fillX().expandX();
		components.row();
		components.add(Creator.sound).fillX().expandX();
		components.row();
		components.add(Creator.music).fillX().expandX();
		components.row();
		components.add(Creator.script).fillX().expandX();
		components.row();
		components.add(Creator.camera).fillX().expandX();
		components.row();
		components.add(Creator.resource).fillX().expandX();
		components.row();

		ScrollPane lookPane = new ScrollPane(components, skin);
		lookPane.setCancelTouchFocus(false);
		lookPane.setScrollingDisabled(true, false);

		table.add(but);
		table.add(lookPane).expand().fill();

		table.setSize(icons, h - icons * 2);
		table.setPosition(w - icons, icons);

		// expand
		but.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (right.getWidth() == icons) {
					right.setPosition(w - 350, icons);
					right.setSize(350, h - icons * 2);
				} else {
					right.setPosition(w - icons, icons);
					right.setSize(icons, h - icons * 2);
				}
			};
		});

		table.setBackground("square");
		return table;
	}

}
