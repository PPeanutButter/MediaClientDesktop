import net.harawata.appdirs.AppDirsFactory

object AppDirTest {
    @JvmStatic
    fun main(args: Array<String>) {
        val appDirs = AppDirsFactory.getInstance()
        println("User data dir: " + appDirs.getUserDataDir("myapp", "1.2.3", "harawata"))
        System.out.println(
            "User data dir (roaming): "
                    + appDirs.getUserDataDir("myapp", "1.2.3", "harawata", true)
        )
        System.out.println(
            "User config dir: "
                    + appDirs.getUserConfigDir("myapp", "1.2.3", "harawata")
        )
        System.out.println(
            "User config dir (roaming): "
                    + appDirs.getUserConfigDir("myapp", "1.2.3", "harawata", true)
        )
        System.out.println(
            "User cache dir: "
                    + appDirs.getUserCacheDir("myapp", "1.2.3", "harawata")
        )
        System.out.println(
            "User log dir: "
                    + appDirs.getUserLogDir("myapp", "1.2.3", "harawata")
        )
        System.out.println(
            "Site data dir: "
                    + appDirs.getSiteDataDir("myapp", "1.2.3", "harawata")
        )
        System.out.println(
            "Site data dir (multi path): "
                    + appDirs.getSiteDataDir("myapp", "1.2.3", "harawata", true)
        )
        System.out.println(
            "Site config dir: "
                    + appDirs.getSiteConfigDir("myapp", "1.2.3", "harawata")
        )
        System.out.println(
            "Site config dir (multi path): "
                    + appDirs.getSiteConfigDir("myapp", "1.2.3", "harawata", true)
        )
        System.out.println(
            "Shared dir: "
                    + appDirs.getSharedDir("myapp", "1.2.3", "harawata")
        )
    }
}