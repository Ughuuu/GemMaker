package com.ngeen.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;

public class AnimationComponent extends Component {
	public Array<AtlasRegion> regions;
	public AtlasRegion region;
	public String page;
	public int index;
	public int resource_index;
}
