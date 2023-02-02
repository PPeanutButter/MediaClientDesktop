package ui.component

fun Windows(func: () -> Unit){
    if (System.getProperty("os.name").contains("Windows")){
        func()
    }
}

fun Linux(func: () -> Unit){
    if (System.getProperty("os.name").contains("Linux")){
        func()
    }
}

fun MacOS(func: () -> Unit){
    if (System.getProperty("os.name").contains("Mac OS")){
        func()
    }
}