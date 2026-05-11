package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {

        FileSearch s = new FileSearch();

        s.putFile("/data/pics/photoA.jpg", 4);
        s.putFile("/data/pics/movie.mp4", 12);
        s.putFile("/work/docs/readme.md", 1);
        s.putFile("/work/docs/report.xml", 7);

        System.out.println(s.fullTraversal());

//        Update (overwrite) size
        s.putFile("/data/pics/photoA.jpg", 9);

//        Search Criteria 1: files > 8 MB inside /data
        System.out.println(s.search(1, "/data", "8"));
//        Returns: ["/data/pics/photoA.jpg", "/data/pics/movie.mp4"]

//        Search Criteria 2: files with ".xml" inside /work
        System.out.println(s.search(2, "/work", ".xml"));
//        Returns: ["/work/docs/report.xml"]


        s.putFile("/media/images/aa.jpg", 6);
        s.putFile("/media/images/ab.jpg", 7);
        s.putFile("/media/images/ac.xml", 2);
        s.putFile("/office/reports/r1.xml", 9);
        s.putFile("/office/reports/r2.xml", 4);
        s.putFile("/office/notes.txt", 3);

//        Search Criteria 1 under a subdirectory
        System.out.println(s.search(1, "/media/images", "5"));
//        Returns: ["/media/images/aa.jpg", "/media/images/ab.jpg"]

//        Search Criteria 2 under a parent directory (recursive)
        System.out.println(s.search(2, "/office", ".xml"));
//        Returns: ["/office/reports/r1.xml", "/office/reports/r2.xml"]
    }
}
