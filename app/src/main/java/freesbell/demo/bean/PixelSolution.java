package freesbell.demo.bean;

public class PixelSolution{
    static final int PIC_QCIF = 0;
    static final int PIC_CIF = 1;
    static final int PIC_2CIF = 2;
    static final int PIC_HD1 = 3;
    static final int PIC_D1 = 4;
    static final int PIC_960H = 5;
    
    public static final int PIC_QVGA = 6;    /* 320 * 240 */
    public static final int PIC_VGA = 7;     /* 640 * 480 */    
    static final int PIC_XGA = 8;     /* 1024 * 768 */   
    static final int PIC_SXGA = 9;    /* 1400 * 1050 */    
    static final int PIC_UXGA = 10;    /* 1600 * 1200 */    
    static final int PIC_QXGA = 11;    /* 2048 * 1536 */

    static final int PIC_WVGA = 12;    /* 854 * 480 */
    static final int PIC_WSXGA = 13;   /* 1680 * 1050 */      
    static final int PIC_WUXGA = 14;   /* 1920 * 1200 */
    static final int PIC_WQXGA = 15;   /* 2560 * 1600 */
    
    public static final int PIC_HD720 = 16;   /* 1280 * 720 */
    static final int PIC_HD1080 = 17;  /* 1920 * 1080 */
    
    static final int PIC_BUTT = 18;
    public static int getWidth(int type){
    	switch(type){
    	case PIC_QCIF:
    		return 176;
    	case PIC_CIF:
    		return 352;
    	case PIC_2CIF:
    		return 704;
    	case PIC_HD1:
    		return 352;
    	case PIC_D1:
    		return 704;
    	case PIC_960H:
    		return 960;
    	case PIC_QVGA:    /* 320 * 240 */
    		return 320;
    	case PIC_VGA:     /* 640 * 480 */  
    		return 640;
    	case PIC_XGA:     /* 1024 * 768 */
    		return 1024;
    	case PIC_SXGA:    /* 1400 * 1050 */  
    		return 1400;
    	case PIC_UXGA:    /* 1600 * 1200 */
    		return 1600;
    	case PIC_QXGA:    /* 2048 * 1536 */
    		return 2048;
    	case PIC_WVGA:    /* 854 * 480 */
    		return 854;
    	case PIC_WSXGA:   /* 1680 * 1050 */ 
    		return 1680;
    	case PIC_WUXGA:   /* 1920 * 1200 */
    		return 1920;
    	case PIC_WQXGA:   /* 2560 * 1600 */
    		return 2560;
    	case PIC_HD720:   /* 1280 * 720 */
    		return 1280;
    	case PIC_HD1080:  /* 1920 * 1080 */
    		return 1920;
    	default:;
    	}
    	return 320;
    }
    public static int getHeight(int type){
    	switch(type){
    	case PIC_QCIF:
    		return 144;
    	case PIC_CIF:
    		return 288;
    	case PIC_2CIF:
    		return 576;
    	case PIC_HD1:
    		return 576;
    	case PIC_D1:
    		return 576;
    	case PIC_960H:
    		return 576;
    	case PIC_QVGA:    /* 320 * 240 */
    		return 240;
    	case PIC_VGA:     /* 640 * 480 */  
    		return 480;
    	case PIC_XGA:     /* 1024 * 768 */
    		return 768;
    	case PIC_SXGA:    /* 1400 * 1050 */  
    		return 1050;
    	case PIC_UXGA:    /* 1600 * 1200 */
    		return 1200;
    	case PIC_QXGA:    /* 2048 * 1536 */
    		return 1536;
    	case PIC_WVGA:    /* 854 * 480 */
    		return 480;
    	case PIC_WSXGA:   /* 1680 * 1050 */ 
    		return 1050;
    	case PIC_WUXGA:   /* 1920 * 1200 */
    		return 1200;
    	case PIC_WQXGA:   /* 2560 * 1600 */
    		return 1600;
    	case PIC_HD720:   /* 1280 * 720 */
    		return 720;
    	case PIC_HD1080:  /* 1920 * 1080 */
    		return 1080;
    	default:;
    	}
    	return 240;
    }
    public static String getFormatString(int pf){
    	switch(pf){
    	case PIC_QCIF:
    		return "176X144";
    	case PIC_CIF:
    		return "352X288";
    	case PIC_2CIF:
    		return "704X576";
    	case PIC_HD1:
    		return "352X576";
    	case PIC_D1:
    		return "704X576";
    	case PIC_960H:
    		return "960X576";
    	case PIC_QVGA:    /* 320 * 240 */
    		return "320X240";
    	case PIC_VGA:     /* 640 * 480 */  
    		return "640X480";
    	case PIC_XGA:     /* 1024 * 768 */
    		return "1024X768";
    	case PIC_SXGA:    /* 1400 * 1050 */  
    		return "1400X1050";
    	case PIC_UXGA:    /* 1600 * 1200 */
    		return "1600X1200";
    	case PIC_QXGA:    /* 2048 * 1536 */
    		return "2048X1536";
    	case PIC_WVGA:    /* 854 * 480 */
    		return "854X480";
    	case PIC_WSXGA:   /* 1680 * 1050 */ 
    		return "1680X1050";
    	case PIC_WUXGA:   /* 1920 * 1200 */
    		return "1920X1200";
    	case PIC_WQXGA:   /* 2560 * 1600 */
    		return "2560X1600";
    	case PIC_HD720:   /* 1280 * 720 */
    		return "1280X720";
    	case PIC_HD1080:  /* 1920 * 1080 */
    		return "1920X1080";
    	default:;
    	}
    	return "320X240(default)";
    }
}
