package cowzgonecrazy.megawallstools.hudproperty;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.Sets;
import cowzgonecrazy.megawallstools.hudproperty.test.DelayedTask;
import cowzgonecrazy.megawallstools.hudproperty.util.ScreenPosition;

import cowzgonecrazy.megawallstools.MegaWallsTools;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class HudPropertyApi {

	public static HudPropertyApi newInstance(){
		HudPropertyApi api = new HudPropertyApi();
		MinecraftForge.EVENT_BUS.register(api);
		return api;
	}

	private Set<cowzgonecrazy.megawallstools.hudproperty.IRenderer> registeredRenderers = Sets.newHashSet();
	private Minecraft mc = Minecraft.getMinecraft();

	private boolean renderOutlines = true;

	private HudPropertyApi(){}

	public void register(cowzgonecrazy.megawallstools.hudproperty.IRenderer... renderers){
		IRenderer[] var2 = renderers;
		int var3 = renderers.length;

		for(int var4 = 0; var4 < var3; ++var4) {
			IRenderer renderer = var2[var4];
			this.registeredRenderers.add(renderer);
		}
	}

	public void unregister(cowzgonecrazy.megawallstools.hudproperty.IRenderer... renderers){
		IRenderer[] var2 = renderers;
		int var3 = renderers.length;

		for(int var4 = 0; var4 < var3; ++var4) {
			IRenderer renderer = var2[var4];
			this.registeredRenderers.remove(renderer);
		}
	}

	public Collection<cowzgonecrazy.megawallstools.hudproperty.IRenderer> getHandlers(){
		return Sets.newHashSet(registeredRenderers);
	}

	public boolean getRenderOutlines(){
		return renderOutlines;
	}

	public void setRenderOutlines(boolean renderOutlines){
		this.renderOutlines = renderOutlines;
	}

	public void openConfigScreen(){
		mc.displayGuiScreen(new cowzgonecrazy.megawallstools.hudproperty.PropertyScreen(this));
	}

	@SubscribeEvent
	public void onRender(RenderGameOverlayEvent event){
		if(event.type == ElementType.TEXT){
			if(!(mc.currentScreen instanceof cowzgonecrazy.megawallstools.hudproperty.PropertyScreen)){
				registeredRenderers.forEach(this::callRenderer);
			}
		}
	}

	private void callRenderer(IRenderer renderer){
		if(!renderer.isEnabled()){
			return;
		}
		
		ScreenPosition position = renderer.load();

		if(position == null){
			position = ScreenPosition.fromRelativePosition(0.5, 0.5);
		}

		renderer.render(position);
	}

}
