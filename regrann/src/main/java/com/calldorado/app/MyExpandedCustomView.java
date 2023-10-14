package com.calldorado.app;

/**
public class MyExpandedCustomView extends CalldoradoCustomView {
    private LinearLayout ll;
    private final Context context;

    public MyExpandedCustomView(Context context) {
        super(context);
        this.context = context;
    }

    private void checkForPostLaterPhotos() {
        KeptListAdapter dbHelper = KeptListAdapter.getInstance(context);
        Cursor cursor = dbHelper.fetchAllItems();

        try {


            if (cursor != null && cursor.getCount() > 0) {
                displayListView();


            }
        } catch (Exception e) {


        }


    }


    public void printCursorData(SimpleCursorAdapter simpleCursorAdapter) {
        Cursor cursor = simpleCursorAdapter.getCursor();

        if (cursor != null) {
            cursor.moveToFirst();

            int col = 0;
            while (!cursor.isAfterLast()) {
                int columnCount = cursor.getColumnCount();


                String toShow = cursor.getString(2);
                Log.d("app5", "file = " + toShow);

                ImageView img = null;
                if (col == 0)
                    img = ll.findViewById(R.id.imageView1);
                if (col == 1)
                    img = ll.findViewById(R.id.imageView2);
                if (col == 2)
                    img = ll.findViewById(R.id.imageView3);
                if (col == 3)
                    img = ll.findViewById(R.id.imageView4);


                col++;
                img.setImageBitmap(Util.decodeFile(new File(toShow)));


                cursor.moveToNext();
            }

            cursor.close();
        } else {
            System.out.println("The cursor is null.");
        }
    }


    private void displayListView() {
        KeptListAdapter dbHelper;
        dbHelper = KeptListAdapter.getInstance(this.context);

        Cursor cursor = dbHelper.fetchAllItems();


        // The desired columns to be bound
        final String[] columns = new String[]{KeptListAdapter.KEY_TITLE, KeptListAdapter.KEY_PHOTO, KeptListAdapter.KEY_AUTHOR, KeptListAdapter.KEY_VIDEOTXT};


        // the XML defined views which the data will be bound to
        int[] to = new int[]{R.id.keptListTitle, R.id.keptListPhoto, R.id.keptListAuthor, R.id.videoIcon};

        // create the adapter using the cursor pointing to the desired data
        // as well as the layout information
        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this.context, R.layout.kept_list_item2, cursor, columns, to, 0);

        printCursorData(dataAdapter);
        if (dataAdapter.isEmpty()) {

            return;
        }
    }


    @Override
    public View getRootView() {

        ll = (LinearLayout) inflate(context, R.layout.
                aftercall_native_layout, null);

        View b = ll.findViewById(R.id.textv);

        checkForPostLaterPhotos();


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {


                    Intent playerActivity = new Intent(context, RegrannMainActivity.class);
                    playerActivity.putExtra("from_afterscreen", true);
                    playerActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    try {


                        context.startActivity(playerActivity);


                    } catch (Exception e) {
    int i = 1;
    }
    } catch (Exception e) {
    int i = 2;
    }
    }
    });
    return ll;
    }
    }
 **/