package com.jince.site.form;

import com.jince.site.entity.Post;
import com.jince.site.entity.PostStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 管理后台里文章表单的输入内容。
 */
@Data
public class PostForm {

	@NotBlank(message = "请填写网址名")
	@Size(max = 100, message = "网址名不能超过 100 个字符")
	@Pattern(regexp = "[a-z0-9]+(-[a-z0-9]+)*", message = "只能用小写英文字母、数字和连字符，例如 my-first-post")
	private String slug;

	@NotBlank(message = "请填写标题")
	@Size(max = 200, message = "标题不能超过 200 个字")
	private String title;

	@Size(max = 500, message = "摘要不能超过 500 个字")
	private String summary;

	@NotBlank(message = "请填写正文")
	private String bodyMd;

	@NotNull(message = "请选择状态")
	private PostStatus status = PostStatus.DRAFT;

	public static PostForm from(Post post) {
		PostForm form = new PostForm();
		form.setSlug(post.getSlug());
		form.setTitle(post.getTitle());
		form.setSummary(post.getSummary());
		form.setBodyMd(post.getBodyMd());
		form.setStatus(post.getStatus());
		return form;
	}

	public void applyTo(Post post) {
		post.setSlug(slug);
		post.setTitle(title);
		post.setSummary(summary);
		post.setBodyMd(bodyMd);
		post.setStatus(status);
	}

}
