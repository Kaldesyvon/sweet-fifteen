package sk.esten.uss.gbco2.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sk.esten.uss.gbco2.dto.response.MenuDto
import sk.esten.uss.gbco2.mapper.MenuMapper
import sk.esten.uss.gbco2.model.entity.menu.VMenuTranslated
import sk.esten.uss.gbco2.model.entity.menu_fav.MenuFav
import sk.esten.uss.gbco2.model.repository.menu.MenuEntityRepository
import sk.esten.uss.gbco2.model.repository.menu.MenuViewRepository
import sk.esten.uss.gbco2.model.repository.menu_fav.MenuFavRepository
import sk.esten.uss.gbco2.utils.getOrValidationException
import sk.esten.uss.gbco2.utils.principal
import sk.esten.uss.gbco2.utils.principalLangOrEn
import sk.esten.uss.gbco2.utils.toMenuTree

@Service
class MenuService(
    val menuEntityRepository: MenuEntityRepository,
    val menuViewRepository: MenuViewRepository,
    val menuFavRepository: MenuFavRepository,
    val menuMapper: MenuMapper
) {

    @Transactional(readOnly = true)
    fun findMenuForUser(): List<MenuDto> {
        return menuViewRepository
            .findAllByEnabledIsTrueAndLanguageIdOrderByMenuOrder(principalLangOrEn().id)
            .toMenuTree()
            .map { menu: VMenuTranslated -> menuMapper.map(menu) }
    }

    @Transactional(readOnly = true)
    fun findFavouriteMenuForUser(): List<MenuDto> {
        return menuViewRepository.findAllByIdAndLanguage(
                menuFavRepository.findAllByUserId(principal()?.id).mapNotNull { menuFav ->
                    menuFav.menuId
                },
                principalLangOrEn().id
            )
            .map { menu: VMenuTranslated -> menuMapper.map(menu) }
    }

    @Transactional
    fun addUserFavouriteMenuItem(path: String) {
        menuFavRepository.save(
            MenuFav().apply {
                userId = principal()?.id
                menuId =
                    menuEntityRepository
                        .findByItemUrl(path)
                        ?.id
                        .getOrValidationException("Menu with path [$path] does not exists")
            }
        )
    }

    @Transactional
    fun removeUserFavouriteMenuItem(path: String) {
        menuFavRepository.delete(
            MenuFav().apply {
                userId = principal()?.id
                menuId =
                    menuEntityRepository
                        .findByItemUrl(path)
                        ?.id
                        .getOrValidationException("Menu with path [$path] does not exists")
            }
        )
    }
}
