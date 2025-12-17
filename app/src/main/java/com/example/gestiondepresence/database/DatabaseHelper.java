package com.example.gestiondepresence.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.gestiondepresence.models.Attendance;
import com.example.gestiondepresence.models.ClassRoom;
import com.example.gestiondepresence.models.Seance;
import com.example.gestiondepresence.models.Student;
import com.example.gestiondepresence.models.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AttendanceDB";
    private static final int DATABASE_VERSION = 2; // Incremented for new table

    // Table Users
    private static final String TABLE_USERS = "users";
    private static final String USER_ID = "id";
    private static final String USER_EMAIL = "email";
    private static final String USER_PASSWORD = "password";

    // Table Students
    private static final String TABLE_STUDENTS = "students";
    private static final String STUDENT_ID = "id";
    private static final String STUDENT_NAME = "name";
    private static final String STUDENT_SURNAME = "surname";
    private static final String STUDENT_GROUP = "group_name";

    // Table Classes
    private static final String TABLE_CLASSES = "classes";
    private static final String CLASS_ID = "id";
    private static final String CLASS_NAME = "class_name";
    private static final String CLASS_DESCRIPTION = "description";

    // Table Seances (NEW)
    private static final String TABLE_SEANCES = "seances";
    private static final String SEANCE_ID = "id";
    private static final String SEANCE_CLASS_ID = "class_id";
    private static final String SEANCE_CLASS_NAME = "class_name";
    private static final String SEANCE_SUBJECT = "subject_name";
    private static final String SEANCE_NUMBER = "seance_number";
    private static final String SEANCE_DATE = "date";
    private static final String SEANCE_TIME = "time";
    private static final String SEANCE_STATUS = "status";

    // Table Attendance
    private static final String TABLE_ATTENDANCE = "attendance";
    private static final String ATTENDANCE_ID = "id";
    private static final String ATTENDANCE_STUDENT_ID = "student_id";
    private static final String ATTENDANCE_DATE = "date";
    private static final String ATTENDANCE_STATUS = "status";
    private static final String ATTENDANCE_SESSION = "session_name";
    private static final String ATTENDANCE_SEANCE_ID = "seance_id"; // NEW: Link to seance

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users table
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + "("
                + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + USER_EMAIL + " TEXT UNIQUE,"
                + USER_PASSWORD + " TEXT"
                + ")";
        db.execSQL(createUsersTable);

        // Create Classes table
        String createClassesTable = "CREATE TABLE " + TABLE_CLASSES + "("
                + CLASS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CLASS_NAME + " TEXT,"
                + CLASS_DESCRIPTION + " TEXT"
                + ")";
        db.execSQL(createClassesTable);

        // Create Students table
        String createStudentsTable = "CREATE TABLE " + TABLE_STUDENTS + "("
                + STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + STUDENT_NAME + " TEXT,"
                + STUDENT_SURNAME + " TEXT,"
                + STUDENT_GROUP + " TEXT"
                + ")";
        db.execSQL(createStudentsTable);

        // Create Seances table (NEW)
        String createSeancesTable = "CREATE TABLE " + TABLE_SEANCES + "("
                + SEANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SEANCE_CLASS_ID + " INTEGER,"
                + SEANCE_CLASS_NAME + " TEXT,"
                + SEANCE_SUBJECT + " TEXT,"
                + SEANCE_NUMBER + " INTEGER,"
                + SEANCE_DATE + " TEXT,"
                + SEANCE_TIME + " TEXT,"
                + SEANCE_STATUS + " TEXT DEFAULT 'active',"
                + "FOREIGN KEY(" + SEANCE_CLASS_ID + ") REFERENCES " + TABLE_CLASSES + "(" + CLASS_ID + ")"
                + ")";
        db.execSQL(createSeancesTable);

        // Create Attendance table (UPDATED with seance_id)
        String createAttendanceTable = "CREATE TABLE " + TABLE_ATTENDANCE + "("
                + ATTENDANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ATTENDANCE_STUDENT_ID + " INTEGER,"
                + ATTENDANCE_DATE + " TEXT,"
                + ATTENDANCE_STATUS + " TEXT,"
                + ATTENDANCE_SESSION + " TEXT,"
                + ATTENDANCE_SEANCE_ID + " INTEGER,"
                + "FOREIGN KEY(" + ATTENDANCE_STUDENT_ID + ") REFERENCES " + TABLE_STUDENTS + "(" + STUDENT_ID + "),"
                + "FOREIGN KEY(" + ATTENDANCE_SEANCE_ID + ") REFERENCES " + TABLE_SEANCES + "(" + SEANCE_ID + ")"
                + ")";
        db.execSQL(createAttendanceTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Add seances table
            String createSeancesTable = "CREATE TABLE IF NOT EXISTS " + TABLE_SEANCES + "("
                    + SEANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + SEANCE_CLASS_ID + " INTEGER,"
                    + SEANCE_CLASS_NAME + " TEXT,"
                    + SEANCE_SUBJECT + " TEXT,"
                    + SEANCE_NUMBER + " INTEGER,"
                    + SEANCE_DATE + " TEXT,"
                    + SEANCE_TIME + " TEXT,"
                    + SEANCE_STATUS + " TEXT DEFAULT 'active'"
                    + ")";
            db.execSQL(createSeancesTable);

            // Add seance_id column to attendance if not exists
            try {
                db.execSQL("ALTER TABLE " + TABLE_ATTENDANCE + " ADD COLUMN " + ATTENDANCE_SEANCE_ID + " INTEGER DEFAULT 0");
            } catch (Exception e) {
                // Column might already exist
            }
        }
    }

    // ==================== USER OPERATIONS ====================

    public boolean registerUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_EMAIL, user.getEmail());
        values.put(USER_PASSWORD, user.getPassword());
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{USER_ID},
                USER_EMAIL + "=? AND " + USER_PASSWORD + "=?",
                new String[]{email, password}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean checkUserExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{USER_ID},
                USER_EMAIL + "=?", new String[]{email}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // ==================== SEANCE OPERATIONS (NEW) ====================

    public long addSeance(Seance seance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SEANCE_CLASS_ID, seance.getClassId());
        values.put(SEANCE_CLASS_NAME, seance.getClassName());
        values.put(SEANCE_SUBJECT, seance.getSubjectName());
        values.put(SEANCE_NUMBER, seance.getSeanceNumber());
        values.put(SEANCE_DATE, seance.getDate());
        values.put(SEANCE_TIME, seance.getTime());
        values.put(SEANCE_STATUS, seance.getStatus());
        return db.insert(TABLE_SEANCES, null, values);
    }

    public Seance getSeanceById(int seanceId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SEANCES, null,
                SEANCE_ID + "=?", new String[]{String.valueOf(seanceId)},
                null, null, null);

        Seance seance = null;
        if (cursor.moveToFirst()) {
            seance = new Seance();
            seance.setId(cursor.getInt(0));
            seance.setClassId(cursor.getInt(1));
            seance.setClassName(cursor.getString(2));
            seance.setSubjectName(cursor.getString(3));
            seance.setSeanceNumber(cursor.getInt(4));
            seance.setDate(cursor.getString(5));
            seance.setTime(cursor.getString(6));
            seance.setStatus(cursor.getString(7));
        }
        cursor.close();
        return seance;
    }

    public List<Seance> getSeancesByClass(int classId) {
        List<Seance> seanceList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SEANCES, null,
                SEANCE_CLASS_ID + "=?", new String[]{String.valueOf(classId)},
                null, null, SEANCE_DATE + " DESC, " + SEANCE_TIME + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Seance seance = new Seance();
                seance.setId(cursor.getInt(0));
                seance.setClassId(cursor.getInt(1));
                seance.setClassName(cursor.getString(2));
                seance.setSubjectName(cursor.getString(3));
                seance.setSeanceNumber(cursor.getInt(4));
                seance.setDate(cursor.getString(5));
                seance.setTime(cursor.getString(6));
                seance.setStatus(cursor.getString(7));
                seanceList.add(seance);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return seanceList;
    }

    public List<Seance> getAllSeances() {
        List<Seance> seanceList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SEANCES + " ORDER BY " + SEANCE_DATE + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                Seance seance = new Seance();
                seance.setId(cursor.getInt(0));
                seance.setClassId(cursor.getInt(1));
                seance.setClassName(cursor.getString(2));
                seance.setSubjectName(cursor.getString(3));
                seance.setSeanceNumber(cursor.getInt(4));
                seance.setDate(cursor.getString(5));
                seance.setTime(cursor.getString(6));
                seance.setStatus(cursor.getString(7));
                seanceList.add(seance);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return seanceList;
    }

    public int getNextSeanceNumber(int classId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(" + SEANCE_NUMBER + ") FROM " + TABLE_SEANCES +
                " WHERE " + SEANCE_CLASS_ID + "=?", new String[]{String.valueOf(classId)});

        int nextNumber = 1;
        if (cursor.moveToFirst() && !cursor.isNull(0)) {
            nextNumber = cursor.getInt(0) + 1;
        }
        cursor.close();
        return nextNumber;
    }

    public boolean updateSeanceStatus(int seanceId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SEANCE_STATUS, status);
        int result = db.update(TABLE_SEANCES, values, SEANCE_ID + "=?",
                new String[]{String.valueOf(seanceId)});
        return result > 0;
    }

    public boolean deleteSeance(int seanceId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Also delete related attendance records
        db.delete(TABLE_ATTENDANCE, ATTENDANCE_SEANCE_ID + "=?",
                new String[]{String.valueOf(seanceId)});
        int result = db.delete(TABLE_SEANCES, SEANCE_ID + "=?",
                new String[]{String.valueOf(seanceId)});
        return result > 0;
    }

    // ==================== STUDENT OPERATIONS ====================

    public boolean addStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (!classExists(student.getGroup())) {
            return false;
        }
        ContentValues values = new ContentValues();
        values.put(STUDENT_NAME, student.getName());
        values.put(STUDENT_SURNAME, student.getSurname());
        values.put(STUDENT_GROUP, student.getGroup());
        long result = db.insert(TABLE_STUDENTS, null, values);
        return result != -1;
    }

    public boolean classExists(String className) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CLASSES, new String[]{CLASS_ID},
                CLASS_NAME + "=?", new String[]{className}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public List<Student> getAllStudents() {
        List<Student> studentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_STUDENTS, null);
        if (cursor.moveToFirst()) {
            do {
                Student student = new Student();
                student.setId(cursor.getInt(0));
                student.setName(cursor.getString(1));
                student.setSurname(cursor.getString(2));
                student.setGroup(cursor.getString(3));
                studentList.add(student);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return studentList;
    }

    public boolean updateStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (!classExists(student.getGroup())) {
            return false;
        }
        ContentValues values = new ContentValues();
        values.put(STUDENT_NAME, student.getName());
        values.put(STUDENT_SURNAME, student.getSurname());
        values.put(STUDENT_GROUP, student.getGroup());
        int result = db.update(TABLE_STUDENTS, values, STUDENT_ID + "=?",
                new String[]{String.valueOf(student.getId())});
        return result > 0;
    }

    public boolean deleteStudent(int studentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_STUDENTS, STUDENT_ID + "=?",
                new String[]{String.valueOf(studentId)});
        return result > 0;
    }

    public List<Student> getStudentsByClass(int classId) {
        List<Student> studentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor classCursor = db.query(TABLE_CLASSES, new String[]{CLASS_NAME},
                CLASS_ID + "=?", new String[]{String.valueOf(classId)}, null, null, null);

        if (classCursor.moveToFirst()) {
            String className = classCursor.getString(0);
            classCursor.close();

            Cursor cursor = db.query(TABLE_STUDENTS, null,
                    STUDENT_GROUP + "=?", new String[]{className}, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    Student student = new Student();
                    student.setId(cursor.getInt(0));
                    student.setName(cursor.getString(1));
                    student.setSurname(cursor.getString(2));
                    student.setGroup(cursor.getString(3));
                    studentList.add(student);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } else {
            classCursor.close();
        }
        return studentList;
    }

    public List<Student> getStudentsByClassName(String className) {
        List<Student> studentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STUDENTS, null,
                STUDENT_GROUP + "=?", new String[]{className}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Student student = new Student();
                student.setId(cursor.getInt(0));
                student.setName(cursor.getString(1));
                student.setSurname(cursor.getString(2));
                student.setGroup(cursor.getString(3));
                studentList.add(student);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return studentList;
    }

    // ==================== ATTENDANCE OPERATIONS (UPDATED) ====================

    public boolean addAttendance(Attendance attendance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ATTENDANCE_STUDENT_ID, attendance.getStudentId());
        values.put(ATTENDANCE_DATE, attendance.getDate());
        values.put(ATTENDANCE_STATUS, attendance.getStatus());
        values.put(ATTENDANCE_SESSION, attendance.getSessionName());
        values.put(ATTENDANCE_SEANCE_ID, attendance.getSeanceId());
        long result = db.insert(TABLE_ATTENDANCE, null, values);
        return result != -1;
    }

    public List<Attendance> getAllAttendance() {
        List<Attendance> attendanceList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ATTENDANCE + " ORDER BY " + ATTENDANCE_DATE + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                Attendance attendance = new Attendance();
                attendance.setId(cursor.getInt(0));
                attendance.setStudentId(cursor.getInt(1));
                attendance.setDate(cursor.getString(2));
                attendance.setStatus(cursor.getString(3));
                attendance.setSessionName(cursor.getString(4));
                if (cursor.getColumnCount() > 5) {
                    attendance.setSeanceId(cursor.getInt(5));
                }
                attendanceList.add(attendance);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return attendanceList;
    }

    public List<Attendance> getAttendanceBySeance(int seanceId) {
        List<Attendance> attendanceList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ATTENDANCE, null,
                ATTENDANCE_SEANCE_ID + "=?", new String[]{String.valueOf(seanceId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Attendance attendance = new Attendance();
                attendance.setId(cursor.getInt(0));
                attendance.setStudentId(cursor.getInt(1));
                attendance.setDate(cursor.getString(2));
                attendance.setStatus(cursor.getString(3));
                attendance.setSessionName(cursor.getString(4));
                attendance.setSeanceId(cursor.getInt(5));
                attendanceList.add(attendance);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return attendanceList;
    }

    public List<Attendance> getAttendanceByStudent(int studentId) {
        List<Attendance> attendanceList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ATTENDANCE, null,
                ATTENDANCE_STUDENT_ID + "=?", new String[]{String.valueOf(studentId)},
                null, null, ATTENDANCE_DATE + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Attendance attendance = new Attendance();
                attendance.setId(cursor.getInt(0));
                attendance.setStudentId(cursor.getInt(1));
                attendance.setDate(cursor.getString(2));
                attendance.setStatus(cursor.getString(3));
                attendance.setSessionName(cursor.getString(4));
                if (cursor.getColumnCount() > 5) {
                    attendance.setSeanceId(cursor.getInt(5));
                }
                attendanceList.add(attendance);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return attendanceList;
    }

    public List<Attendance> getAttendanceByDate(String date) {
        List<Attendance> attendanceList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ATTENDANCE, null,
                ATTENDANCE_DATE + "=?", new String[]{date}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Attendance attendance = new Attendance();
                attendance.setId(cursor.getInt(0));
                attendance.setStudentId(cursor.getInt(1));
                attendance.setDate(cursor.getString(2));
                attendance.setStatus(cursor.getString(3));
                attendance.setSessionName(cursor.getString(4));
                if (cursor.getColumnCount() > 5) {
                    attendance.setSeanceId(cursor.getInt(5));
                }
                attendanceList.add(attendance);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return attendanceList;
    }

    public int getAbsenceCount(int studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ATTENDANCE, new String[]{ATTENDANCE_ID},
                ATTENDANCE_STUDENT_ID + "=? AND " + ATTENDANCE_STATUS + "=?",
                new String[]{String.valueOf(studentId), "absent"}, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int getRetardCount(int studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ATTENDANCE, new String[]{ATTENDANCE_ID},
                ATTENDANCE_STUDENT_ID + "=? AND " + ATTENDANCE_STATUS + "=?",
                new String[]{String.valueOf(studentId), "retard"}, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    // ==================== CLASS OPERATIONS ====================

    public boolean addClass(ClassRoom classRoom) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CLASS_NAME, classRoom.getClassName());
        values.put(CLASS_DESCRIPTION, classRoom.getDescription());
        long result = db.insert(TABLE_CLASSES, null, values);
        return result != -1;
    }

    public List<ClassRoom> getAllClasses() {
        List<ClassRoom> classList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CLASSES, null);

        if (cursor.moveToFirst()) {
            do {
                ClassRoom classRoom = new ClassRoom();
                classRoom.setId(cursor.getInt(0));
                classRoom.setClassName(cursor.getString(1));
                classRoom.setDescription(cursor.getString(2));
                classList.add(classRoom);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return classList;
    }

    public ClassRoom getClassById(int classId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CLASSES, null,
                CLASS_ID + "=?", new String[]{String.valueOf(classId)},
                null, null, null);

        ClassRoom classRoom = null;
        if (cursor.moveToFirst()) {
            classRoom = new ClassRoom();
            classRoom.setId(cursor.getInt(0));
            classRoom.setClassName(cursor.getString(1));
            classRoom.setDescription(cursor.getString(2));
        }
        cursor.close();
        return classRoom;
    }

    public boolean updateClass(ClassRoom classRoom) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CLASS_NAME, classRoom.getClassName());
        values.put(CLASS_DESCRIPTION, classRoom.getDescription());
        int result = db.update(TABLE_CLASSES, values, CLASS_ID + "=?",
                new String[]{String.valueOf(classRoom.getId())});
        return result > 0;
    }

    public boolean deleteClass(int classId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_CLASSES, CLASS_ID + "=?",
                new String[]{String.valueOf(classId)});
        return result > 0;
    }
}