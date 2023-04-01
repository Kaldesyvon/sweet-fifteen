package sk.esten.uss.gbco2.utils

import sk.esten.uss.gbco2.model.entity.material.VMaterialTranslated
import sk.esten.uss.gbco2.model.entity.menu.VMenuTranslated
import sk.esten.uss.gbco2.model.entity.node.VNodeTranslated

fun List<VMaterialTranslated>.toMaterialTree(): List<VMaterialTranslated> {
    val root = mutableListOf<VMaterialTranslated>()
    val filteredMenu = this.filter { item -> null == item.parentMaterial }
    filteredMenu.forEach { item ->
        innerTree(item, this.toMutableList())
        root.add(item)
    }

    return root
}

private fun innerTree(parent: VMaterialTranslated, all: List<VMaterialTranslated>) {
    val children = all.filter { item -> item.parentMaterial == parent }.toMutableSet()
    parent.subMaterial = children

    for (child in children) {
        innerTree(child, all)
    }
}

fun List<VNodeTranslated>.toNodeTree(): List<VNodeTranslated> {
    val root = mutableListOf<VNodeTranslated>()
    val filteredMenu = this.filter { item -> null == item.parentNode }
    filteredMenu.forEach { item ->
        innerTree(item, this.toMutableList())
        root.add(item)
    }

    return root
}

private fun innerTree(parent: VNodeTranslated, all: List<VNodeTranslated>) {
    val children = all.filter { item -> item.parentNode == parent }.toMutableSet()
    parent.subNode = children

    for (child in children) {
        innerTree(child, all)
    }
}

fun List<VMenuTranslated>.toMenuTree(): List<VMenuTranslated> {
    val root = mutableListOf<VMenuTranslated>()
    val filteredMenu = this.filter { m -> haveAccessToMenu(m) }
    filteredMenu.forEach { menu ->
        if (null == menu.parentMenu) {
            innerTree(menu, filteredMenu)
            root.add(menu)
        }
    }

    return root.sortedBy { m -> m.menuOrder }
}

private fun innerTree(parent: VMenuTranslated, all: List<VMenuTranslated>) {
    val children =
        all
            .filter { menu -> menu.parentMenu == parent }
            .sortedBy { m -> m.menuOrder }
            .toMutableSet()
    parent.subMenu = children

    for (child in children) {
        innerTree(child, all)
    }
}

private fun haveAccessToMenu(menu: VMenuTranslated): Boolean {
    val menuRoles = menu.menuRoles
    if (menuRoles.isEmpty()) {
        return true
    }

    for (menuRole in menuRoles) {
        val isInRole = menuRole.roleId?.let { hasRole(it) }?.or(false) == true
        if (isInRole) {
            return if (false == menuRole.allPlants || menuRole.allPlants == null) {
                true
            } else {
                val authorizations = principal()?.authorizations
                if (authorizations.isNullOrEmpty()) {
                    false
                } else {
                    authorizations.any { auth ->
                        auth.roleId == menuRole.roleId && auth.nodeId == null
                    }
                }
            }
        }
    }

    return false
}
