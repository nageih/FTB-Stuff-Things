package dev.ftb.mods.ftbobb.blocks;

public interface ProgressProvider {
    int getProgress();
    int getMaxProgress();

    void setProgress(int progress);

    void setMaxProgress(int maxProgress);
}
