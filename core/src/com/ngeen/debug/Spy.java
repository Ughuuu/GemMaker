package com.ngeen.debug;

import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Spy {
	Skin skin;
	int x, y, w = 640, h = 400;
	ArrayList<Cls> classes = new ArrayList<Cls>();
	String[] classesS;
	public Table table;

	public Spy(Skin skin1, String... name) {
		h = Gdx.graphics.getHeight();
		classesS = name;
		classes.clear();
		for (int ind = 0; ind < name.length; ind++) {
			try {
				Class<?> c = Class.forName(name[ind]);
				Package p = c.getPackage();
				Field[] mem = c.getFields();
				ArrayList<Field> extra = new ArrayList<Field>();

				classes.add(new Cls(c.getCanonicalName(), p != null ? p.getName() : "-- default --", mem, null));

			} catch (ClassNotFoundException x) {
				x.printStackTrace();
			}
		}
		skin = skin1;
		createGUI();
		table.setDebug(true);
	}

	public Spy(Skin skin1, Object... obj) {
		h = Gdx.graphics.getHeight();
		String[] str = new String[obj.length];
		for (int i = 0; i < obj.length; i++) {
			str[i] = obj[i].getClass().getName();
		}
		classesS = str;
		classes.clear();
		for (int ind = 0; ind < obj.length; ind++) {
			Class<?> c = obj[ind].getClass();
			Package p = c.getPackage();

			Field[] mem = obj[ind].getClass().getDeclaredFields();

			classes.add(new Cls(c.getCanonicalName(), p != null ? p.getName() : "-- default --", mem, obj[ind]));
		}
		skin = skin1;
		createGUI();
		table.setDebug(true);
	}

	public void recreate(Skin skin1, String... name) {
		h = Gdx.graphics.getHeight();
		classes.clear();
		for (int ind = 0; ind < name.length; ind++) {
			try {
				Class<?> c = Class.forName(name[ind]);
				Package p = c.getPackage();
				Field[] mem = c.getFields();

				classes.add(new Cls(c.getCanonicalName(), p != null ? p.getName() : "-- default --", mem, ind));

			} catch (ClassNotFoundException x) {
				x.printStackTrace();
			}
		}
		skin = skin1;
		createGUI();
		table.setDebug(true);

	}

	Label addLabel(String name) {
		Label label = new Label(name, skin);
		return label;
	}

	TextField addText(String name) {
		TextField text = new TextField(name, skin);
		return text;
	}

	Table addName(String n1, String n2) {
		Table add = new Table(skin);
		Label lb1 = addLabel(n1);
		Label lb2 = addLabel(n2);

		Pixmap col = new Pixmap(1, 1, Format.RGB565);
		if (n2.equals("int") || n2.equals("float") || n2.equals("double") || n2.equals("boolean")) {
			col.setColor(new Color(0.3f, 0.9f, 0.5f, 1));
			add.add(lb1).pad(20);
			add.add(lb2);
		} else if (n2.indexOf("java.lang") != -1) {
			col.setColor(new Color(0.9f, 0.7f, 0.3f, 1));
			add.add(lb1).pad(20);
			add.add(lb2);
		} else if (n2.indexOf("artemis") != -1) {
			return new Table();
		} else {
			col.setColor(new Color(0.2f, 0.2f, 0.2f, 1));
			Spy spy = new Spy(skin, n2);
			table.row();
			add.row();
			add.add(lb1).pad(20);
			add.add(spy.table);
		}
		col.fill();
		add.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(col))));
		return add;
	}

	void createGUI() {

		table = new Table(skin);
		for (int i = 0; i < classes.size(); i++) {
			Cls c = classes.get(i);

			table.add(addLabel(c.name));
			table.add(addLabel(c.pkg));
			for (int j = 0; j < c.size(); j++) {
				((Field) c.field[j]).setAccessible(true);
				if (!((Field) c.field[j]).isAccessible() || Modifier.isFinal(((Field) c.field[j]).getModifiers())
						|| java.lang.reflect.Modifier.isStatic(((Field) c.field[j]).getModifiers()))
					return;
				table.add(addName(c.getName(j), c.getType(j)));
				TextField text = addText(c.getValue(j).toString());
				classes.get(i).text[j] = text.getText();
				text.setName(i + " " + j);
				text.setTextFieldListener(new TextFieldListener() {

					@Override
					public void keyTyped(TextField textField, char c) {
						String name = textField.getName();
						int sp = name.indexOf(" ");
						int i = Integer.parseInt(name.substring(0, sp));
						int j = Integer.parseInt(name.substring(sp + 1, name.length()));
						classes.get(i).setValue(j, classes.get(i).text[j]);

					}
				});
				table.add(text);
				table.row();
			}
		}
		return;
	}
}