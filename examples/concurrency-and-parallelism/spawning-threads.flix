def main(): Unit \ IO =
    region rc {
        spawn (pintln("Hello")) @ rc;
        spawn (println("World")) @ rc
    }
