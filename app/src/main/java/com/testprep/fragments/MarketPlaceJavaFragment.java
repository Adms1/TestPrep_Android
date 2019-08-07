//package com.testprep.fragments;
//
//
//import android.content.Context;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//import butterknife.Bind;
//import butterknife.ButterKnife;
//import com.e.carouseldemo.CarouselParameters;
//import com.testprep.R;
//import com.testprep.carouselPkg.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class MarketPlaceJavaFragment extends Fragment {
//
//    CarouselView carousel, carousel1;
//    TextView lblSelectedIndex;
//    TextView lblSelectedIndex1;
//
//    public MarketPlaceJavaFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_market_place_java, container, false);
//        return rootView;
//    }
//
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
////        final Bundle args = getArguments();
//
//        carousel = (CarouselView) getView().findViewById(R.id.carousel);
//        carousel1 = (CarouselView) getView().findViewById(R.id.carousel1);
//
//        lblSelectedIndex = (TextView) getView().findViewById(R.id.lblSelectedIndex);
//        lblSelectedIndex1 = (TextView) getView().findViewById(R.id.lblSelectedIndex1);
//
////		https://github.com/davidschreiber/FancyCoverFlow.git
//        ViewGroup.LayoutParams lp = carousel.getLayoutParams();
//        ViewGroup.LayoutParams lp1 = carousel1.getLayoutParams();
//
//        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        lp1.width = ViewGroup.LayoutParams.MATCH_PARENT;
//
//        lp.height = (int) Metrics.convertDpToPixel(400f, getActivity());
//        lp1.height = (int) Metrics.convertDpToPixel(400f, getActivity());
//
//        carousel.setLayoutParams(lp);
//        carousel1.setLayoutParams(lp);
//
//        getView().requestLayout();
//
//        carousel.setAdapter(new RandomPageAdapter(5, 330, 160, getActivity()));
//        carousel1.setAdapter(new RandomPageAdapter(5, 330, 160, getActivity()));
//
//        boolean isClipChildren = false;
//        carousel.setClipChildren(isClipChildren);
//        ((ViewGroup) carousel.getParent()).setClipChildren(isClipChildren);
//        ((ViewGroup) carousel.getParent()).setClipToPadding(isClipChildren);
//
//        carousel.setInfinite(true);
//        carousel.setExtraVisibleChilds(0);
//        carousel.getLayoutManager().setDrawOrder(CarouselView.DrawOrder.values()[1]);
//
//        carousel.setGravity(17);
//
//        carousel.setScrollingAlignToViews(true);
//
//        carousel.setOnItemSelectedListener(new CarouselView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(CarouselView carouselView, int position, int adapterPosition, RecyclerView.Adapter adapter) {
//                lblSelectedIndex.setText("Selected Position " + position);
//            }
//
//            @Override
//            public void onItemDeselected(CarouselView carouselView, int position, int adapterPosition, RecyclerView.Adapter adapter) {
//
//            }
//        });
//
//        carousel1.setInfinite(true);
//        carousel1.setExtraVisibleChilds(0);
//        carousel1.getLayoutManager().setDrawOrder(CarouselView.DrawOrder.values()[1]);
//
//        carousel1.setGravity(17);
//
//        carousel1.setScrollingAlignToViews(true);
//
//        carousel1.setOnItemSelectedListener(new CarouselView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(CarouselView carouselView, int position, int adapterPosition, RecyclerView.Adapter adapter) {
//                lblSelectedIndex.setText("Selected Position " + position);
//            }
//
//            @Override
//            public void onItemDeselected(CarouselView carouselView, int position, int adapterPosition, RecyclerView.Adapter adapter) {
//
//            }
//        });
//
//        HashMap<String, Number> hashMap= new HashMap<>();
//        hashMap.put("numPies",5);
//        hashMap.put("farScale",0);
//        hashMap.put("viewPerspective",0.1);
//        hashMap.put("horizontalViewPort",0.75);
//        hashMap.put("farAlpha",0);
//
//        int transformerSelectedPos = 1;
//        CarouselView.ViewTransformer transformer = null;
//        if (transformerSelectedPos < CarouselParameters.TRANSFORMER_CLASSES.size()) {
//            // built-in transformer
//            transformer = CarouselParameters.createTransformer(
//                    CarouselParameters.TRANSFORMER_CLASSES.get(transformerSelectedPos),
//                    hashMap
//            );
//        }
//
//        carousel.setTransformer(transformer);
//        carousel1.setTransformer(transformer);
//
//        carousel.post(new Runnable() {
//            @Override
//            public void run() {
//                // smooth scroll to the 'centermost' item
//                carousel.smoothScrollToPosition(1);
//            }
//        });
//
//        carousel1.post(new Runnable() {
//            @Override
//            public void run() {
//                // smooth scroll to the 'centermost' item
//                carousel1.smoothScrollToPosition(1);
//            }
//        });
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        @Bind(R.id.img)
//        ImageView textView;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//        }
//    }
//
//    public static class RandomPageAdapter extends RecyclerView.Adapter<ViewHolder> {
//        int size, width, height;
//        Context context;
//
//        public RandomPageAdapter(int size, int width, int height, Context context) {
//            super();
//            this.size = size;
//            this.width = width;
//            this.height = height;
//            this.context = context;
//        }
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//            View view = LayoutInflater.from(context).inflate(R.layout.carousel_container, parent, false);
//            ViewHolder holder = new ViewHolder(view);
//            return holder;
//        }
//
//        @Override
//        public void onBindViewHolder(ViewHolder holder, final int position) {
//            RandomPageFragment.initializeTextView(holder.textView, (position + 1));
//
//            holder.textView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(context, "position" + position, Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return 3;
//        }
//    }
//
//}
