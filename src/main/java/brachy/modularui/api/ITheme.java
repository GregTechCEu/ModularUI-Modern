package brachy.modularui.api;

import brachy.modularui.theme.SelectableTheme;
import brachy.modularui.theme.SlotTheme;
import brachy.modularui.theme.TextFieldTheme;
import brachy.modularui.theme.WidgetTheme;
import brachy.modularui.theme.WidgetThemeEntry;
import brachy.modularui.theme.WidgetThemeKey;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * A theme is parsed from JSON and contains style information like color or background texture.<br>
 *
 * A theme consists of data for multiple widget theme keys ({@link WidgetThemeKey},
 * where each theme key represents the theme data for a specific type of widget (e.g. item slot, fluid slot, etc).<br>
 *
 * @see WidgetThemeKey
 * @see WidgetThemeEntry
 */
public interface ITheme {

    Codec<ITheme> REFERENCE_CODEC = Codec.STRING.comapFlatMap(id -> IThemeApi.get().hasTheme(id) ?
            DataResult.success(IThemeApi.get().getTheme(id)) : DataResult.error(() -> "Unknown theme: " + id),
            ITheme::getId);

    Encoder<ITheme> ENCODER = new Encoder<>() {

        @Override
        public <T> DataResult<T> encode(ITheme input, DynamicOps<T> ops, T prefix) {
            var mapBuilder = ops.mapBuilder();
            mapBuilder.add("id", ops.createString(input.getId()));
            mapBuilder.add("parent", ops.createString(input.getParentTheme().getId()));
            input.getFallbackEntry().encode(ops, mapBuilder, true);
            Map<String, Set<WidgetTheme>> encodedThemes = new Object2ObjectOpenHashMap<>();
            for (WidgetThemeEntry<?> entry : input.getWidgetThemeEntries()) {
                if (entry.key() == IThemeApi.FALLBACK) continue;
                var set = encodedThemes.computeIfAbsent(entry.key().getName(), k -> new ObjectOpenHashSet<>());
                if (entry.key().isSubWidgetTheme() && set.contains(entry.theme()) && set.contains(entry.hoverTheme()))
                    continue;
                entry.encode(ops, mapBuilder, false);
                set.add(entry.theme());
                set.add(entry.hoverTheme());
            }
            return mapBuilder.build(prefix);
        }
    };

    /**
     * @return the master default theme.
     */
    static ITheme getDefault() {
        return IThemeApi.get().getDefaultTheme();
    }

    /**
     * @param id theme id
     * @return theme with given id
     */
    static ITheme get(String id) {
        return IThemeApi.get().getTheme(id);
    }

    /**
     * @return theme id
     */
    String getId();

    /**
     * @return parent theme
     */
    ITheme getParentTheme();

    @UnmodifiableView
    Collection<WidgetThemeEntry<?>> getWidgetThemeEntries();

    WidgetThemeEntry<WidgetTheme> getFallbackEntry();

    WidgetThemeEntry<WidgetTheme> getPanelThemeEntry();

    WidgetThemeEntry<WidgetTheme> getButtonThemeEntry();

    WidgetThemeEntry<WidgetTheme> getScrollbarThemeEntry();

    WidgetThemeEntry<SlotTheme> getItemSlotThemeEntry();

    WidgetThemeEntry<SlotTheme> getFluidSlotThemeEntry();

    WidgetThemeEntry<TextFieldTheme> getTextFieldThemeEntry();

    WidgetThemeEntry<SelectableTheme> getToggleButtonThemeEntry();

    <T extends WidgetTheme> WidgetThemeEntry<T> getWidgetThemeEntry(WidgetThemeKey<T> key);
}
