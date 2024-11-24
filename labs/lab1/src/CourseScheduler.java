public class CourseScheduler {

    static final int STARTING_HOUR=0;
    static final int ENDING_HOUR=1;

    private static void sortByStartingHour(int[][] courses) {
        for (int i = 0; i < courses.length - 1; i++) {
            int minElementIndex = i;

            for (int j = i + 1; j < courses.length; j++) {
                if (courses[j][STARTING_HOUR] < courses[minElementIndex][STARTING_HOUR])
                    minElementIndex = j;
            }

            if (minElementIndex != i) {
                int[] temp = courses[i];
                courses[i] = courses[minElementIndex];
                courses[minElementIndex] = temp;
            }

        }
    }

    public static int maxNonOverlappingCourses(int[][] courses){
       sortByStartingHour(courses);

        int maxNonOverlapping=(courses.length>0?1:0);
        int lastConvenientCourseIndex=0;

        for(int i=0;i<courses.length;i++) {
                if (courses[i][STARTING_HOUR] >= courses[lastConvenientCourseIndex][ENDING_HOUR]) {
                    lastConvenientCourseIndex = i;
                    maxNonOverlapping++;
                }
                else
                    lastConvenientCourseIndex=courses[i][ENDING_HOUR]<courses[lastConvenientCourseIndex][ENDING_HOUR]?i:lastConvenientCourseIndex;
            }

        return maxNonOverlapping;
    }
}