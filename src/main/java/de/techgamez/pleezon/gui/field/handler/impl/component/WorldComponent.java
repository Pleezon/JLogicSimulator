package de.techgamez.pleezon.gui.field.handler.impl.component;

import de.techgamez.pleezon.backend.data.LogicComponent;
import de.techgamez.pleezon.backend.data.save.Blottable;
import de.techgamez.pleezon.backend.data.save.BlotterInputStream;
import de.techgamez.pleezon.backend.data.save.BlotterOutputStream;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Objects;

public class WorldComponent implements Blottable
{

	private static class TextureCache
	{
		private static class CacheKey
		{
			public WorldComponent.ComponentState state;
			Class<? extends LogicComponent> clazz;
			boolean actuated = false;

			public CacheKey(Class<? extends LogicComponent> clazz, WorldComponent.ComponentState state, boolean actuated)
			{
				this.clazz = clazz;
				this.state = state;
				this.actuated = actuated;
			}

			@Override
			public boolean equals(Object o)
			{
				if (this == o) return true;
				if (o == null || getClass() != o.getClass()) return false;
				CacheKey cacheKey = (CacheKey) o;
				return state == cacheKey.state && clazz.equals(cacheKey.clazz) && cacheKey.actuated == this.actuated;
			}

			@Override
			public int hashCode()
			{
				return Objects.hash(state, clazz, actuated);
			}
		}

		private final HashMap<CacheKey, BufferedImage> cache = new HashMap<>();

		public BufferedImage retrieve(WorldComponent component) throws IOException
		{
			CacheKey key = new CacheKey(component.component.getClass(), component.state, component.component.getState());
			if (cache.containsKey(key)) {
				return cache.get(key);
			}
			BufferedImage texture = component.texture();
			cache.put(key, texture);
			return texture;
		}
	}

	private static final TextureCache cache = new TextureCache();

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		WorldComponent that = (WorldComponent) o;
		return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0 && component.equals(that.component);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(component.getID());
	}

	public LogicComponent component;
	public double x;
	public double y;

	public enum ComponentState
	{
		NORMAL, SELECTED
	}

	public ComponentState state;

	public WorldComponent(LogicComponent component, int x, int y)
	{
		this.component = component;
		this.x = x;
		this.y = y;
		this.state = ComponentState.NORMAL;
	}

	private BufferedImage retrieveTexture()
	{
		BufferedImage texture;
		try {
			texture = cache.retrieve(this);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return texture;
	}

	public boolean isIn(Rectangle rectangle)
	{
		Rectangle bounds = getTextureBounds();
		return rectangle.intersects(bounds);
	}

	public Rectangle getTextureBounds()
	{
		BufferedImage image = retrieveTexture();
		Rectangle bounds = new Rectangle((int) x, (int) y, image.getWidth(), image.getHeight());
		return bounds;
	}

	public Point2D.Float getMiddlePoint()
	{
		Rectangle r = getTextureBounds();
		return new Point2D.Float((float) r.getCenterX(), (float) r.getCenterY());
	}

	public boolean isInHitbox(Point2D point, boolean ignoreTransparent)
	{
		int x = (int) point.getX();
		int y = (int) point.getY();
		if (x < this.x || y < this.y) {
			return false;
		}
		BufferedImage texture = retrieveTexture();
		x -= this.x;
		y -= this.y;
		if (x >= texture.getWidth() || y >= texture.getHeight()) {
			return false;
		}
		int alpha = ignoreTransparent ? 255 : (texture.getRGB(x, y) >> 24) & 0xff;
		return alpha > 0;
	}

	public void setState(ComponentState state)
	{
		this.state = state;
	}

	public ComponentState getState()
	{
		return state;
	}


	public void unselect()
	{
		setState(ComponentState.NORMAL);
	}


	private BufferedImage texture() throws IOException
	{
		BufferedImage texture = ImageIO.read(Objects.requireNonNull(getClass().getResource(component.texturePath())));
		Color c = this.state == ComponentState.SELECTED ? javax.swing.UIManager.getDefaults().getColor("Component.focusColor") : javax.swing.UIManager.getDefaults().getColor("Component.linkColor");
		if (component.getState()) {
			c = Color.RED;
		}
		for (int x = 0; x < texture.getWidth(); x++) {
			for (int y = 0; y < texture.getHeight(); y++) {
				int argb = texture.getRGB(x, y);
				double medium = ((((argb & 0xFF) + ((argb >> 8) & 0xFF) + ((argb >> 16) & 0xFF)) / 3.0) / 255.0);
				int newARGB = 0;
				newARGB |= (int) (medium * c.getRed());
				newARGB |= (int) (medium * c.getGreen()) << 8;
				newARGB |= (int) (medium * c.getBlue()) << 16;
				newARGB |= (((argb >> 24) & 0xff) << 24);
				texture.setRGB(x, y, newARGB);
			}
		}
		return texture;
	}

	public void draw(Graphics2D g, int offX, int offY)
	{
		try {
			BufferedImage texture = cache.retrieve(this);
			g.drawImage(texture, null, (int) x - offX, (int) y - offY);
			g.drawString(Long.toString(component.getID()), (int) getMiddlePoint().getX(), (int) getMiddlePoint().getY());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void blot(BlotterOutputStream out) throws IOException
	{
		// TODO: optimize by not writing the same data twice (e.g. using IDs instead of class name)
		// these strings are humongous
		out.writeString(component.getClass().getName());
		component.blot(out);
		out.writeDouble(x);
		out.writeDouble(y);
	}

	@Override
	public void unblot(BlotterInputStream in) throws IOException
	{
		try {
			component = (LogicComponent) Class.forName(in.readString()).getDeclaredConstructor().newInstance();
			component.unblot(in);
			x = in.readDouble();
			y = in.readDouble();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | InvocationTargetException |
		         NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

}
