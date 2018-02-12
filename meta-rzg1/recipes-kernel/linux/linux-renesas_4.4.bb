require linux.inc
require linux-dtb.inc
require linux-dtb-append.inc
require linux-config.inc

DESCRIPTION = "Linux kernel for the R-Car Generation 2 based board"
COMPATIBLE_MACHINE = "(skrzg1e|skrzg1m|iwg20m|iwg22m|iwg23s)"

PV_append = "+git${SRCREV}"

RENESAS_URL="git://github.com/renesas-rz/renesas-cip.git"
SRCREV = "32bc9271796e81832764602fc0ee10ed490b4732"
SRC_URI = " \
	${RENESAS_URL};protocol=git;branch=v4.4.55-cip3 \
"

SRC_URI_append_iwg22m = " \
	file://iwg22m/0001-ethavb-workaround-to-fix-PHY-address.patch \
"

SRC_URI_append_iwg23s = " \
	file://iwg23s/0001-ARM-shmobilel-setup-rcar-gen2-resize-CMA-area-1-for-.patch \
"

S = "${WORKDIR}/git"


SRC_URI_append_skrzg1m = " file://skrzg1m.cfg"

PATCHTOOL_rzg1 = "git"

KERNEL_DEFCONFIG = "shmobile_defconfig"

do_configure_prepend() {
    install -m 0644 ${S}/arch/${ARCH}/configs/${KERNEL_DEFCONFIG} ${WORKDIR}/defconfig || die "No default configuration for ${MACHINE} / ${KERNEL_DEFCONFIG} available."
}


do_configure_append() {

	configure_ext3
	configure_ext4
	configure_usb_storage
	configure_touchscreen
	configure_LVDS_panel
	configure_common

	kernel_configure_variable RTC_DRV_BQ32K y
	kernel_configure_variable RTC_LIB y
	kernel_configure_variable RTC_CLASS y
	kernel_configure_variable RTC_HCTOSYS y
	kernel_configure_variable RTC_SYSTOHC y
	kernel_configure_variable RTC_HCTOSYS_DEVICE "rtc0"
	kernel_configure_variable RTC_INTF_SYSFS y
	kernel_configure_variable RTC_INTF_PROC y
	kernel_configure_variable RTC_INTF_DEV y

	# Enable bluetooth suport
	kernel_configure_variable   BT y
	kernel_configure_variable   BT_RFCOMM y
	kernel_configure_variable   BT_RFCOMM_TTY y
	kernel_configure_variable   BT_BNEP y
	kernel_configure_variable   BT_BNEP_MC_FILTER y
	kernel_configure_variable   BT_BNEP_PROTO_FILTER y
	kernel_configure_variable   BT_HIDP y
	kernel_configure_variable   BT_HCIBTUSB y
	kernel_configure_variable   BT_HCIBTSDIO y
	kernel_configure_variable   BT_HCIUART y
	kernel_configure_variable   BT_HCIUART_H4 y
	kernel_configure_variable   BT_HCIUART_BCSP y
	kernel_configure_variable   BT_HCIUART_ATH3K y
	kernel_configure_variable   BT_HCIUART_LL y
	kernel_configure_variable   BT_HCIUART_3WIRE y
	kernel_configure_variable   BT_HCIBCM203X y
	kernel_configure_variable   BT_HCIBPA10X y
	kernel_configure_variable   BT_HCIBFUSB y
	kernel_configure_variable   BT_HCIVHCI y
	kernel_configure_variable   BT_MRVL y
	kernel_configure_variable   BT_MRVL_SDIO y
	kernel_configure_variable   BT_ATH3K y

	yes '' | oe_runmake oldconfig
}

do_configure_append_skrzg1m() {

	configure_cma_skrzg1m

	yes '' | oe_runmake oldconfig
}

do_configure_append_iwg20m() {

	configure_cma_iwg20m
	configure_ravb
	configure_rcar_can
	configure_rcar_pwm

	kernel_configure_variable USB_U_ETHER n
	kernel_configure_variable USB_ETH m
	kernel_configure_variable RTC_DRV_BQ32K y
	kernel_configure_variable SND_SOC_SGTL5000 y
	kernel_configure_variable RENESAS_WDT y
	kernel_configure_variable VIDEO_TVP5150 y
	kernel_configure_variable USB_XHCI_HCD y
	kernel_configure_variable USB_XHCI_PCI y
	kernel_configure_variable USB_XHCI_PLATFORM y
	kernel_configure_variable USB_XHCI_RCAR y
	kernel_configure_variable_str EXTRA_FIRMWARE r8a779x_usb3_v1.dlmem
	kernel_configure_variable MEDIA_USB_SUPPORT y
	kernel_configure_variable MEDIA_CAMERA_SUPPORT y
	kernel_configure_variable USB_VIDEO_CLASS y
	kernel_configure_variable VIDEO_V4L2 y
	kernel_configure_variable USB_VIDEO_CLASS y
	kernel_configure_variable SOC_CAMERA_OV772X y
	kernel_configure_variable TOUCHSCREEN_EDT_FT5X06 y

# configure for USB OTG
	kernel_configure_variable USB_OHCI_LITTLE_ENDIAN y
	kernel_configure_variable USB_GADGETFS m
	kernel_configure_variable USB_LIBCOMPOSITE m
	kernel_configure_variable USB_MASS_STORAGE m
	kernel_configure_variable USB_F_MASS_STORAGE m
	kernel_configure_variable USB_EHCI_ROOT_HUB_TT y
	kernel_configure_variable USB_XHCI_HCD_DEBUGGING y
	kernel_configure_variable USB_OTG y
	kernel_configure_variable USB_ARCH_HAS_OHCI y
	kernel_configure_variable USB_ARCH_HAS_EHCI y
	kernel_configure_variable USB_ARCH_HAS_XHCI y

	yes '' | oe_runmake oldconfig
}

do_configure_append_iwg22m() {
	configure_ravb

	kernel_configure_variable STMPE_I2C y
	kernel_configure_variable MFD_STMPE y
	kernel_configure_variable TOUCHSCREEN_STMPE y
	kernel_configure_variable TOUCHSCREEN_STMPE_CALIBRATION_WORKAROUND y
	kernel_configure_variable SOC_CAMERA_OV772X y
	kernel_configure_variable USB_OHCI_LITTLE_ENDIAN y
	kernel_configure_variable USB_GADGETFS m
	kernel_configure_variable USB_LIBCOMPOSITE m
	kernel_configure_variable USB_MASS_STORAGE m
	kernel_configure_variable USB_F_MASS_STORAGE m
	kernel_configure_variable USB_EHCI_ROOT_HUB_TT y
	kernel_configure_variable USB_XHCI_HCD_DEBUGGING y
	kernel_configure_variable USB_OTG y
	kernel_configure_variable USB_ARCH_HAS_OHCI y
	kernel_configure_variable USB_ARCH_HAS_EHCI y
	kernel_configure_variable USB_ARCH_HAS_XHCI y

	yes '' | oe_runmake oldconfig
}

do_configure_append_iwg23s() {
	kernel_configure_variable DRM_I2C_SII902X y

	yes '' | oe_runmake oldconfig
}

do_configure_append_iwg23s() {

	kernel_configure_variable USB_OHCI_LITTLE_ENDIAN y
	kernel_configure_variable USB_GADGETFS m
	kernel_configure_variable USB_LIBCOMPOSITE m
	kernel_configure_variable USB_MASS_STORAGE m
	kernel_configure_variable USB_F_MASS_STORAGE m
	kernel_configure_variable USB_EHCI_ROOT_HUB_TT y
	kernel_configure_variable USB_OTG y
	kernel_configure_variable USB_ARCH_HAS_OHCI y
	kernel_configure_variable USB_OHCI_HCD_PLATFORM y
	kernel_configure_variable USB_EHCI_HCD_PLATFORM y
	kernel_configure_variable USB_EHCI_ROOT_HUB_TT y
	kernel_configure_variable USB_ARCH_HAS_EHCI y

	yes '' | oe_runmake oldconfig
}
## for gles-kernel-module
do_compile_append() {
	cp ${KBUILD_OUTPUT}/vmlinux ${STAGING_KERNEL_BUILDDIR}/vmlinux

}
