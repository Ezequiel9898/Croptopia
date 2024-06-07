package com.epherical.croptopia.datagen;

import com.epherical.croptopia.common.generator.ConfiguredFeatureKeys;
import com.epherical.croptopia.common.generator.PlacedFeatureKeys;
import com.epherical.croptopia.register.helpers.Tree;
import com.epherical.croptopia.register.helpers.TreeCrop;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.concurrent.CompletableFuture;

public class CroptopiaWorldGeneration extends FabricDynamicRegistryProvider {


    public CroptopiaWorldGeneration(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        HolderLookup.RegistryLookup<ConfiguredFeature<?, ?>> lookup = registries.lookupOrThrow(Registries.CONFIGURED_FEATURE);
        HolderLookup.RegistryLookup<PlacedFeature> placed = registries.lookupOrThrow(Registries.PLACED_FEATURE);
        for (TreeCrop treeCrop : TreeCrop.TREE_CROPS) {
            addConfiguredFeature(treeCrop.getConfiguredFeatureKey(), treeCrop.getTreeConfig(), entries);
            entries.add(treeCrop.getPlacedFeatureKey(), new PlacedFeature(lookup.getOrThrow(treeCrop.getConfiguredFeatureKey()), WorldGenFeatures.datagenModifierLists.get(treeCrop.getPlacedFeatureKey())));
        }

        for (Tree tree : Tree.copy()) {
            addConfiguredFeature(tree.getConfiguredFeatureKey(), tree.getTreeGen(), entries);
            entries.add(tree.getPlacedFeatureKey(), new PlacedFeature(lookup.getOrThrow(tree.getConfiguredFeatureKey()), WorldGenFeatures.datagenModifierLists.get(tree.getPlacedFeatureKey())));
        }

        addConfiguredFeature(ConfiguredFeatureKeys.DISK_SALT_KEY, WorldGenFeatures.DISK_SALT, entries);
        entries.add(PlacedFeatureKeys.DISK_SALT_PLACED_KEY, new PlacedFeature(lookup.getOrThrow(ConfiguredFeatureKeys.DISK_SALT_KEY), WorldGenFeatures.datagenModifierLists.get(PlacedFeatureKeys.DISK_SALT_PLACED_KEY)));


        addConfiguredFeature(ConfiguredFeatureKeys.RANDOM_CROP_KEY, WorldGenFeatures.RANDOM_CROP, entries);
        entries.add(PlacedFeatureKeys.RANDOM_CROP_KEY, new PlacedFeature(lookup.getOrThrow(ConfiguredFeatureKeys.RANDOM_CROP_KEY), WorldGenFeatures.datagenModifierLists.get(PlacedFeatureKeys.RANDOM_CROP_KEY)));
    }

    @Override
    public String getName() {
        return "Croptopia World Gen";
    }


    private Holder<ConfiguredFeature<?, ?>> addConfiguredFeature(ResourceKey<ConfiguredFeature<?, ?>> key, ConfiguredFeature<?, ?> config, Entries entries) {
        return entries.add(key, config);
    }

}
