package brachy.modularui.theme;

import brachy.modularui.api.ITheme;
import brachy.modularui.api.IThemeApi;

import lombok.Getter;

public abstract class AbstractTheme implements ITheme {

    @Getter
    private final String id;
    @Getter
    private final ITheme parentTheme;

    private WidgetThemeEntry<WidgetTheme> fallback;
    private WidgetThemeEntry<WidgetTheme> panel;
    private WidgetThemeEntry<WidgetTheme> button;
    private WidgetThemeEntry<WidgetTheme> scrollbar;
    private WidgetThemeEntry<SlotTheme> itemSlot;
    private WidgetThemeEntry<SlotTheme> fluidSlot;
    private WidgetThemeEntry<TextFieldTheme> textField;
    private WidgetThemeEntry<SelectableTheme> toggleButtonTheme;

    protected AbstractTheme(String id, ITheme parentTheme) {
        this.id = id;
        this.parentTheme = parentTheme;
    }

    @Override
    public WidgetThemeEntry<WidgetTheme> getFallbackEntry() {
        if (fallback == null) {
            fallback = getWidgetThemeEntry(IThemeApi.FALLBACK);
        }
        return fallback;
    }

    @Override
    public WidgetThemeEntry<WidgetTheme> getPanelThemeEntry() {
        if (panel == null) {
            panel = getWidgetThemeEntry(IThemeApi.PANEL);
        }
        return panel;
    }

    @Override
    public WidgetThemeEntry<WidgetTheme> getButtonThemeEntry() {
        if (button == null) {
            button = getWidgetThemeEntry(IThemeApi.BUTTON);
        }
        return button;
    }

    @Override
    public WidgetThemeEntry<WidgetTheme> getScrollbarThemeEntry() {
        if (scrollbar == null) {
            scrollbar = getWidgetThemeEntry(IThemeApi.SCROLLBAR);
        }
        return scrollbar;
    }

    @Override
    public WidgetThemeEntry<SlotTheme> getItemSlotThemeEntry() {
        if (itemSlot == null) {
            itemSlot = getWidgetThemeEntry(IThemeApi.ITEM_SLOT);
        }
        return itemSlot;
    }

    @Override
    public WidgetThemeEntry<SlotTheme> getFluidSlotThemeEntry() {
        if (fluidSlot == null) {
            fluidSlot = getWidgetThemeEntry(IThemeApi.FLUID_SLOT);
        }
        return fluidSlot;
    }

    @Override
    public WidgetThemeEntry<TextFieldTheme> getTextFieldThemeEntry() {
        if (textField == null) {
            textField = getWidgetThemeEntry(IThemeApi.TEXT_FIELD);
        }
        return textField;
    }

    @Override
    public WidgetThemeEntry<SelectableTheme> getToggleButtonThemeEntry() {
        if (toggleButtonTheme == null) {
            toggleButtonTheme = getWidgetThemeEntry(IThemeApi.TOGGLE_BUTTON);
        }
        return toggleButtonTheme;
    }
}
