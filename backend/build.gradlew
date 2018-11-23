jar {
    manifest {
        attributes 'Main-Class': 'ru.hse.spb.kazakov.MainKt'
    }
    from { configurations.compile.collect { it.isDirectory() ? it : zipTree(it) } }
}
